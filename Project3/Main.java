import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        // Use a hard-coded Pokémon name for the checkpoint.
        String pokemonName = "pikachu";

        try {
            System.out.println(fetchPokemonJson(pokemonName));
        } catch (IOException e) {
            System.err.println("Could not retrieve Pokémon data. " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("The PokéAPI request was interrupted.");
        }
    }

    private static String fetchPokemonJson(String pokemonName)
            throws IOException, InterruptedException {
        String encodedName = URLEncoder.encode(pokemonName, StandardCharsets.UTF_8);
        URI uri = URI.create("https://pokeapi.co/api/v2/pokemon/" + encodedName);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("PokéAPI returned HTTP " + response.statusCode() + ".");
        }
        return response.body();
    }
}
