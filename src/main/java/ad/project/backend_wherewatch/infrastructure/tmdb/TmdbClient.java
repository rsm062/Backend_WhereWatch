package ad.project.backend_wherewatch.infrastructure.tmdb;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class TmdbClient {

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/search/movie";

    private final ObjectMapper objectMapper;

    public TmdbClient() {
        this.objectMapper = new ObjectMapper();
    }

    public List<MovieDTO> searchMovies(String title, String apiKey) throws IOException, InterruptedException {
        URI uri = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL)
                .queryParam("api_key", apiKey)
                .queryParam("query", title)
                .queryParam("language", "es-ES")
                .build()
                .toUri();
        System.err.println("Llamando a TMDB con URL: " + uri.toString());

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en llamada TMDB: " + response.statusCode());
        }

        String body = response.body();

        JsonNode root = objectMapper.readTree(body);
        JsonNode results = root.path("results");

        List<MovieDTO> movies = new ArrayList<>();

        for (JsonNode movieNode : results) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setTitle(movieNode.path("title").asText());
            movieDTO.setOverview(movieNode.path("overview").asText());
            movieDTO.setReleaseDate(movieNode.path("release_date").asText(""));
            /*movieDTO.setPosterPath(movieNode.path("poster_path").asText(null));
            movieDTO.setRating(movieNode.path("vote_average").asDouble(0.0));*/
            // Por ahora dejamos availabilities vacías, se pueden cargar manualmente o luego se extiende
            movies.add(movieDTO);
        }

        return movies;
    }
}

