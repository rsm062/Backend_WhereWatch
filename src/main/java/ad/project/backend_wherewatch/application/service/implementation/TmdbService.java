package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.CountryDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.dto.PlatformDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.InterfaceTmdbService;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.models.Country;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService implements InterfaceTmdbService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieService movieService;

    @Autowired
    private final MovieRepository movieRepository;


    @Autowired
    private PlatformService platformService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private final ObjectMapper objectMapper;

    private final String TMDB_API_KEY = "346172216d64b089ac0f2697e41ab7fa";
    private final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?query={title}&api_key={apiKey}";

    public TmdbService(MovieRepository movieRepository, ObjectMapper objectMapper) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public MovieDTO fetchAndStoreMovieByTitle(String title) throws JsonProcessingException {
        // 1. Fetch basic movie info
        ResponseEntity<String> response = restTemplate.getForEntity(
                TMDB_SEARCH_URL, String.class, title, TMDB_API_KEY);

        // 2. Parse response into MovieDTO (aquí usarías tu DTO y parser, o una clase intermedia)
        // En este ejemplo asumimos que tienes ya un método de parsing, si no lo creamos luego.

        MovieDTO movieDTO = parseResponse(response.getBody());

        // 3. Convert DTO to entity
        Movie movie = movieMapper.toEntity(movieDTO);

        // 4. Save platforms and countries through services, avoid duplicates
        for (var availabilityDTO : movieDTO.getAvailabilities()) {
            Platform platform = platformService.findByName(availabilityDTO.getPlatform().getName())
                    .orElseGet(() -> platformService.save(
                            new Platform(0, availabilityDTO.getPlatform().getName(), availabilityDTO.getPlatform().getLogoPath())));

            Country country = countryService.findByIsoCode(availabilityDTO.getCountry().getIsoCode())
                    .orElseGet(() -> countryService.save(
                            new Country(0, availabilityDTO.getCountry().getName(), availabilityDTO.getCountry().getIsoCode())));

            Availability availability = new Availability();
            availability.setMovie(movie);
            availability.setPlatform(platform);
            availability.setCountry(country);

            movie.getAvailabilities().add(availability);
        }

        // 5. Save movie (cascades availabilities)
        Movie savedMovie = movieService.save(movie);

        return movieMapper.toDto(savedMovie);
    }

    private MovieDTO parseResponse(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(json);

        Long movieId = root.path("id").asLong();
        JsonNode resultsNode = root.path("results");

        List<AvailabilityDTO> availabilities = new ArrayList<>();

        // Iteramos por país
        Iterator<Map.Entry<String, JsonNode>> countries = resultsNode.fields();
        while (countries.hasNext()) {
            Map.Entry<String, JsonNode> countryEntry = countries.next();
            String countryCode = countryEntry.getKey();
            JsonNode countryNode = countryEntry.getValue();

            JsonNode flatrate = countryNode.path("flatrate");
            if (flatrate.isMissingNode() || !flatrate.isArray()) continue;

            // Creamos CountryDTO
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setIsoCode(countryCode);
             // función opcional

            for (JsonNode providerNode : flatrate) {
                PlatformDTO platformDTO = new PlatformDTO();
                platformDTO.setId(providerNode.path("provider_id").asInt());
                platformDTO.setName(providerNode.path("provider_name").asText());
                platformDTO.setLogoPath(providerNode.path("logo_path").asText());

                AvailabilityDTO availability = new AvailabilityDTO();
                availability.setCountry(countryDTO);
                availability.setPlatform(platformDTO);

                availabilities.add(availability);
            }
        }

        // Creamos MovieDTO (ejemplo con campos ficticios porque este endpoint no da title, etc.)
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movieId);
        movieDTO.setTitle("Título desde otro endpoint o hardcoded"); // Puedes combinar respuestas
        movieDTO.setOverview("Resumen desde otro endpoint");
        movieDTO.setReleaseDate("2024-01-01");
/*movieDTO.setPosterPath("/poster.jpg");
        movieDTO.setRating(7.5);*/
        movieDTO.setAvailabilities(availabilities);

        return movieDTO;
    }

    @Override
    public List<MovieDTO> searchAndSaveMoviesByTitle(String title) {
        try {
            String url = TMDB_SEARCH_URL + UriUtils.encode(title, StandardCharsets.UTF_8);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            List<MovieDTO> movieDTOs = new ArrayList<>();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            for (JsonNode movieNode : results) {
                MovieDTO dto = new MovieDTO();
                dto.setTitle(movieNode.path("title").asText());
                dto.setOverview(movieNode.path("overview").asText(null));
                /*dto.setPosterPath(movieNode.path("poster_path").asText(null));*/
                dto.setReleaseDate(movieNode.path("release_date").asText(null));
                /*dto.setRating(movieNode.path("vote_average").asDouble(0.0));*/
                dto.setAvailabilities(new ArrayList<>()); // Vacío al principio

                Movie movie = movieMapper.toEntity(dto);
                movieRepository.save(movie);
                movieDTOs.add(movieMapper.toDto(movie));
            }

            return movieDTOs;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching or saving movies", e);
        }
    }

}

