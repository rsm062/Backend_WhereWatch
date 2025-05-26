package ad.project.backend_wherewhatch.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TmdbService {
    private final WebClient webClient;
    private final String apiKey = "fc8867fdaadbd8be898d992a70cecf08";

    public TmdbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public String searchMovie(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
