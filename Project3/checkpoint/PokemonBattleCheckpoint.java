import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Initial Project 3 checkpoint program for a two-Pokemon battle game.
 * The main method tests one PokéAPI request. The remaining methods show
 * how the final program will read input, prepare the Pokemon, and run a battle.
 */
public class PokemonBattleCheckpoint {
    /** Requests the Pikachu record and prints the raw JSON on HTTP 200. */
    public static void main(String[] args) {
        String pokemonName = "pikachu";

        try {
            System.out.println(fetchPokemonJson(pokemonName));
        } catch (IOException exception) {
            System.err.println("Could not get Pokemon data. " + exception.getMessage());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.err.println("The PokéAPI request was interrupted.");
        }
    }

    /**
     * Sends a GET request to PokéAPI and reads its response body.
     * @param pokemonName the lowercase PokéAPI name to look up
     * @return the raw JSON text returned by PokéAPI
     * @throws IOException if the request fails or the status is something other than 200
     * @throws InterruptedException if the request is interrupted
     */
    private static String fetchPokemonJson(String pokemonName)
            throws IOException, InterruptedException {
        URI uri = URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonName);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("PokéAPI returned HTTP " + response.statusCode() + ".");
        }
        return response.body();
    }

    /**
     * Plans a prompt that accepts a Pokemon name or picks a random Pokemon.
     * @param input the Scanner used to read the player's choice
     * @param random the random number generator used to pick a Pokemon ID
     * @param prompt the text asking for the player or opponent Pokemon
     * @return the Pokemon selected for the battle
     */
    private static Pokemon choosePokemon(Scanner input, Random random, String prompt) {
        // Read the name or pick an ID from 1 to 151, then call fetchPokemon.
        throw new UnsupportedOperationException("Planned for the final project.");
    }

    /**
     * Plans the conversion from PokéAPI JSON into a Java Pokemon object.
     * @param name the Pokemon name or ID to look up
     * @return a Pokemon with its types and base stats
     */
    private static Pokemon fetchPokemon(String name) {
        // Call fetchPokemonJson and parse the types and stats arrays.
        throw new UnsupportedOperationException("Planned for the final project.");
    }

    /**
     * Plans the lookup of damage relationships for a Pokemon's attack types.
     * @param pokemon the Pokemon whose types need to be looked up
     * @param typeChart the map of attack types to their damage multipliers
     */
    private static void loadTypes(Pokemon pokemon, Map<String, Map<String, Double>> typeChart) {
        // Read the damage relations from PokéAPI and save them for later turns.
    }

    /**
     * Plans the rounds of the battle until one Pokemon runs out of HP.
     * @param input the Scanner used to read battle actions
     * @param player the Pokemon the player controls
     * @param opponent the Pokemon the computer controls
     * @param typeChart the map used to look up type effectiveness
     */
    private static void playBattle(Scanner input, Pokemon player, Pokemon opponent,
                                   Map<String, Map<String, Double>> typeChart) {
        // Compare Speed, call the turn methods in order, and announce the winner.
    }

    /**
     * Plans a turn in which the player picks an attack or uses a potion.
     * @param input the Scanner used to read the player's action
     * @param player the Pokemon taking this turn
     * @param opponent the target of a player attack
     * @param typeChart the map used to look up type effectiveness
     */
    private static void playerTurn(Scanner input, Pokemon player, Pokemon opponent,
                                   Map<String, Map<String, Double>> typeChart) {
        // Show the available actions and either calculate damage or restore HP.
    }

    /**
     * Plans a turn in which the computer picks its strongest attack.
     * @param opponent the Pokemon taking this turn
     * @param player the target of the opponent's attack
     * @param typeChart the map used to look up type effectiveness
     */
    private static void opponentTurn(Pokemon opponent, Pokemon player,
                                     Map<String, Map<String, Double>> typeChart) {
        // Compare possible attacks and apply the one that deals the most damage.
    }

    /**
     * Plans the damage calculation for a single attack.
     * @param attacker the Pokemon using the move
     * @param defender the target of the move
     * @param attack the selected strike
     * @param typeChart the map used to look up type effectiveness
     * @return the amount of HP to subtract from the defender
     */
    private static int calculateDamage(Pokemon attacker, Pokemon defender, String attack,
                                       Map<String, Map<String, Double>> typeChart) {
        // Combine Attack, Defense, and the multiplier for each of the defender's types.
        throw new UnsupportedOperationException("Planned for the final project.");
    }

    /** Plans the Java object that holds one Pokemon's data and battle state. */
    private static class Pokemon {
        // Store the name, types, current and maximum HP, Attack, Defense, and Speed.
    }
}
