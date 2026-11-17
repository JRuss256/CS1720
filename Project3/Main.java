import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    private static final String API_URL = "https://pokeapi.co/api/v2/";
    private static final int KANTO_POKEMON_COUNT = 151;
    private static final String QUICK_STRIKE = "quick-strike";
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        System.out.println("=== Pokemon Battle ===");
        System.out.println("Enter a Pokemon name, or type random (Enter also works).");

        try (Scanner input = new Scanner(System.in)) {
            Random random = new Random();
            Pokemon player = choosePokemon(input, random, "Your Pokemon");
            if (player == null) {
                return;
            }
            Pokemon opponent = choosePokemon(input, random, "Opponent Pokemon");
            if (opponent == null) {
                return;
            }

            Map<String, Map<String, Double>> typeChart = new HashMap<>();
            loadTypes(player, typeChart);
            loadTypes(opponent, typeChart);
            playBattle(input, player, opponent, typeChart);
        } catch (IOException e) {
            System.err.println("Battle unavailable. " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Battle unavailable. PokéAPI returned unexpected data.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("The PokéAPI request was interrupted.");
        }
    }

    private static Pokemon choosePokemon(Scanner input, Random random, String prompt)
            throws IOException, InterruptedException {
        while (true) {
            System.out.print(prompt + ": ");
            if (!input.hasNextLine()) {
                System.out.println("Input ended. Battle cancelled.");
                return null;
            }

            String name = input.nextLine().trim().toLowerCase(Locale.ROOT)
                    .replace(".", "").replaceAll("\\s+", "-");
            boolean randomPick = name.isEmpty() || name.equals("random");
            if (randomPick) {
                name = Integer.toString(random.nextInt(KANTO_POKEMON_COUNT) + 1);
            } else if (!name.matches("[a-z0-9]+(-[a-z0-9]+)*")) {
                System.out.println("Use a name such as pikachu or mr-mime.");
                continue;
            }

            Pokemon pokemon = fetchPokemon(name);
            if (pokemon != null) {
                System.out.println(prompt + " selected: " + pokemon.name
                        + " (" + String.join("/", pokemon.types) + ")");
                return pokemon;
            }
            if (randomPick) {
                throw new IOException("PokéAPI did not return Pokemon #" + name + ".");
            }
            System.out.println("Pokemon not found. Try another name or type random.");
        }
    }

    private static JSONObject fetchJson(String endpoint, boolean allowMissing)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(API_URL + endpoint))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404 && allowMissing) {
            return null;
        }
        if (response.statusCode() != 200) {
            throw new IOException("PokéAPI returned HTTP " + response.statusCode()
                    + " for " + endpoint + ".");
        }
        return new JSONObject(response.body());
    }

    private static Pokemon fetchPokemon(String name) throws IOException, InterruptedException {
        JSONObject data = fetchJson("pokemon/" + name, true);
        if (data == null) {
            return null;
        }

        List<String> types = new ArrayList<>();
        JSONArray typeEntries = data.getJSONArray("types");
        for (int i = 0; i < typeEntries.length(); i++) {
            types.add(typeEntries.getJSONObject(i).getJSONObject("type").getString("name"));
        }

        Map<String, Integer> stats = new HashMap<>();
        JSONArray statEntries = data.getJSONArray("stats");
        for (int i = 0; i < statEntries.length(); i++) {
            JSONObject entry = statEntries.getJSONObject(i);
            stats.put(entry.getJSONObject("stat").getString("name"), entry.getInt("base_stat"));
        }

        if (types.isEmpty() || !stats.keySet().containsAll(List.of("hp", "attack", "defense", "speed"))) {
            throw new JSONException("Pokemon data is missing a type or a required stat.");
        }
        return new Pokemon(data.getString("name"), types, stats);
    }

    private static void loadTypes(Pokemon pokemon, Map<String, Map<String, Double>> typeChart)
            throws IOException, InterruptedException {
        for (String attackType : pokemon.types) {
            if (!typeChart.containsKey(attackType)) {
                JSONObject data = fetchJson("type/" + attackType, false);
                JSONObject relations = data.getJSONObject("damage_relations");
                Map<String, Double> multipliers = new HashMap<>();
                addMultipliers(multipliers, relations.getJSONArray("double_damage_to"), 2.0);
                addMultipliers(multipliers, relations.getJSONArray("half_damage_to"), 0.5);
                addMultipliers(multipliers, relations.getJSONArray("no_damage_to"), 0.0);
                typeChart.put(attackType, multipliers);
            }
        }
    }

    private static void addMultipliers(Map<String, Double> multipliers, JSONArray types, double value) {
        for (int i = 0; i < types.length(); i++) {
            multipliers.put(types.getJSONObject(i).getString("name"), value);
        }
    }

    private static void playBattle(Scanner input, Pokemon player, Pokemon opponent,
            Map<String, Map<String, Double>> typeChart) {
        boolean playerFirst = player.speed >= opponent.speed;
        System.out.println("\n" + (playerFirst ? player.name : opponent.name) + " moves first.");
        int round = 1;
        while (player.hp > 0 && opponent.hp > 0) {
            System.out.println("\n--- Round " + round + " ---");
            System.out.println(player.name + " (you): " + player.hp + "/" + player.maxHp + " HP");
            System.out.println(opponent.name + " (opponent): " + opponent.hp + "/" + opponent.maxHp + " HP");

            if (!playerFirst) {
                opponentTurn(opponent, player, typeChart);
            }
            if (player.hp > 0 && !playerTurn(input, player, opponent, typeChart)) {
                System.out.println("Input ended. Battle cancelled.");
                return;
            }
            if (playerFirst && opponent.hp > 0) {
                opponentTurn(opponent, player, typeChart);
            }
            round++;
        }
        System.out.println(player.hp > 0 ? "\nYou win!" : "\nYou lose!");
    }

    private static boolean playerTurn(Scanner input, Pokemon player, Pokemon opponent,
            Map<String, Map<String, Double>> typeChart) {
        for (int i = 0; i < player.attacks.size(); i++) {
            System.out.println((i + 1) + ". " + attackName(player.attacks.get(i)));
        }
        int potionChoice = player.attacks.size() + 1;
        if (player.hasPotion) {
            System.out.println(potionChoice + ". Potion (heal up to 25% of max HP, once per battle)");
        }

        while (true) {
            System.out.print("Choose an action: ");
            if (!input.hasNextLine()) {
                return false;
            }
            String answer = input.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(answer);
            } catch (NumberFormatException e) {
                System.out.println("Enter one of the listed numbers.");
                continue;
            }
            if (choice >= 1 && choice <= player.attacks.size()) {
                strike(player, opponent, player.attacks.get(choice - 1), typeChart);
                return true;
            }
            if (choice == potionChoice && player.hasPotion) {
                if (player.hp == player.maxHp) {
                    System.out.println("HP is already full. Choose another action.");
                    continue;
                }
                int restored = Math.min(player.maxHp - player.hp, Math.max(1, player.maxHp / 4));
                player.hp += restored;
                player.hasPotion = false;
                System.out.println(player.name + " restored " + restored + " HP.");
                return true;
            }
            System.out.println("Enter one of the listed numbers.");
        }
    }

    private static void opponentTurn(Pokemon opponent, Pokemon player,
            Map<String, Map<String, Double>> typeChart) {
        String bestAttack = QUICK_STRIKE;
        int bestDamage = calculateDamage(opponent, player, bestAttack, typeChart);
        for (String attack : opponent.types) {
            int damage = calculateDamage(opponent, player, attack, typeChart);
            if (damage > bestDamage) {
                bestAttack = attack;
                bestDamage = damage;
            }
        }
        strike(opponent, player, bestAttack, typeChart);
    }

    private static void strike(Pokemon attacker, Pokemon defender, String attack,
            Map<String, Map<String, Double>> typeChart) {
        int damage = calculateDamage(attacker, defender, attack, typeChart);
        double multiplier = effectiveness(attack, defender, typeChart);
        defender.hp = Math.max(0, defender.hp - damage);
        System.out.printf(Locale.ROOT, "%s used %s. %d damage (%.2fx type). %s has %d/%d HP.%n",
                attacker.name, attackName(attack), damage, multiplier, defender.name, defender.hp, defender.maxHp);
    }

    private static int calculateDamage(Pokemon attacker, Pokemon defender, String attack,
            Map<String, Map<String, Double>> typeChart) {
        double multiplier = effectiveness(attack, defender, typeChart);
        if (multiplier == 0.0) {
            return 0;
        }
        int power = attack.equals(QUICK_STRIKE) ? 24 : 36;
        double attackShare = (double) attacker.attack / (attacker.attack + defender.defense);
        return Math.max(1, (int) Math.round(power * attackShare * multiplier));
    }

    private static double effectiveness(String attack, Pokemon defender,
            Map<String, Map<String, Double>> typeChart) {
        if (attack.equals(QUICK_STRIKE)) {
            return 1.0;
        }
        double multiplier = 1.0;
        for (String type : defender.types) {
            multiplier *= typeChart.get(attack).getOrDefault(type, 1.0);
        }
        return multiplier;
    }

    private static String attackName(String attack) {
        if (attack.equals(QUICK_STRIKE)) {
            return "Quick strike";
        }
        return capitalize(attack) + " strike";
    }

    private static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static class Pokemon {
        final String name;
        final List<String> types;
        final List<String> attacks;
        final int maxHp;
        final int attack;
        final int defense;
        final int speed;
        int hp;
        boolean hasPotion = true;

        Pokemon(String name, List<String> types, Map<String, Integer> stats) {
            this.name = capitalize(name);
            this.types = types;
            attacks = new ArrayList<>(types);
            attacks.add(QUICK_STRIKE);
            maxHp = stats.get("hp") + 30;
            hp = maxHp;
            attack = stats.get("attack");
            defense = stats.get("defense");
            speed = stats.get("speed");
        }
    }
}
