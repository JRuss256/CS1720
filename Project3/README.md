# Pokémon Battle

Choose a Pokémon and an opponent, then battle until one runs out of HP. PokéAPI supplies both Pokémon's types, base stats, and type matchups.

## Run the game

Use JDK 17, Maven, and an internet connection. From this folder, run:

```sh
mvn -q compile exec:java
```

If your class environment already provides `org.json`, use its configured classpath to run `Main.java`.

For a first battle, enter `pikachu` and `gyarados`, then select **Electric strike**. Either selection also accepts `random` or an empty line. Each random choice picks one of the 151 original Pokémon.

## Battle rules

- The Pokémon with higher Speed goes first each round. The player goes first on a tie.
- Each Pokémon has a strike for each of its types and a weaker, always-neutral Quick strike. Quick strike uses the same turn order as the other attacks.
- A typed strike multiplies the effectiveness against each of the defender's types. An immune matchup deals zero damage.
- Starting HP is the PokéAPI base HP plus 30. Damage uses the attack's power, the attacker’s Attack, the defender’s Defense, and type effectiveness.
- The player gets one potion that can restore up to one quarter of their maximum HP. Using it takes a turn. The computer picks its highest-damage attack.

## Example API requests

For Pikachu versus Gyarados, the game asks for [Pikachu's stats and types](https://pokeapi.co/api/v2/pokemon/pikachu) and [Gyarados's stats and types](https://pokeapi.co/api/v2/pokemon/gyarados). Then it fetches the damage relations for [Electric](https://pokeapi.co/api/v2/type/electric), Water, and Flying. Entering a different name changes which Pokémon the program downloads.
