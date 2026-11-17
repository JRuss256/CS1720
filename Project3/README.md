# Pokémon Type Matchup Calculator

## Project 3A description

My Java program will ask the user to enter two Pokémon names. It will use PokéAPI to look up each Pokémon's types and the damage relationships for those types. For each Pokémon, the program will calculate how effective an attack of each of its own types would be against the other Pokémon. If the defender has two types, the program will multiply the two effectiveness values. It will show the strongest attack type for each side and compare their multipliers. If the highest multipliers are equal, it will call the matchup a tie.

## Example API requests

If the user enters **Pikachu** and **Gyarados**, the final program will start with these requests:

- [Pikachu](https://pokeapi.co/api/v2/pokemon/pikachu)
- [Gyarados](https://pokeapi.co/api/v2/pokemon/gyarados)

Gyarados is Water/Flying. Since both types are weak to Electric, a Pikachu Electric-type attack gets a `2 × 2 = 4×` multiplier.

## Run the checkpoint

The checkpoint uses a hard-coded name, `pikachu`. The `fetchPokemonJson` method sends the request, checks the HTTP status, and returns the response body when the status is 200. Then `main` prints that raw JSON.
