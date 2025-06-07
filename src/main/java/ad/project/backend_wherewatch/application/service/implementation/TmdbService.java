package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.CountryDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.dto.PlatformDTO;
import ad.project.backend_wherewatch.application.mapper.AvailabilityMapper;
import ad.project.backend_wherewatch.application.mapper.CountryMapper;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.mapper.PlatformMapper;
import ad.project.backend_wherewatch.application.service.InterfaceTmdbService;
import ad.project.backend_wherewatch.domain.models.*;
import ad.project.backend_wherewatch.domain.repositories.AvailabilityRepository;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TmdbService implements InterfaceTmdbService {

    private static final Logger logger = LoggerFactory.getLogger(TmdbService.class);
    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;
    private final AsyncSaverService asyncSaverService;
    private final PlatformService platformService;
    private final CountryService countryService;
    private final MovieMapper movieMapper;
    private final AvailabilityRepository availabilityRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tmdb.api.key}")
    private String apiKey; // Token completo con 'Bearer ...'


    @Autowired
    public TmdbService(RestTemplate restTemplate, MovieRepository movieRepository, AsyncSaverService asyncSaverService, PlatformService platformService, CountryService countryService, MovieMapper movieMapper, AvailabilityRepository availabilityRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
        this.asyncSaverService = asyncSaverService;
        this.platformService = platformService;
        this.countryService = countryService;
        this.movieMapper = movieMapper;
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Searches for movies on TMDb by title.
     *
     * @param title The movie title or partial title to search for.
     * @return A list of MovieDTO objects representing the found movies,
     *         or an empty list if no results are found or an error occurs.
     */
    @Override
    public List<MovieDTO> searchMovies(String title) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("query", title)
                //.queryParam("include_adult", true)
                //.queryParam("language", "es-ES")
                .queryParam("page", 1)
                .toUriString();

        ResponseEntity<String> response = headerCreater(url);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("TMDb API search failed with status: {}", response.getStatusCode());
            return Collections.emptyList();
        }
        return parseResponse(response.getBody());
    }

    /**
     * Executes an authorized HTTP GET request to the given URL.
     *
     * @param url The complete URL of the endpoint.
     * @return A ResponseEntity containing the JSON response body.
     */
    private ResponseEntity<String> headerCreater(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            logger.error("HTTP request failed for URL: {}", url, e);
            throw e;
        }
    }

    /**
     * Parses the JSON response from TMDb to extract a list of movies.
     *
     * @param json The JSON string response.
     * @return A list of MovieDTO objects parsed from the response,
     *         or an empty list if no movies are found or an error occurs.
     */
    private List<MovieDTO> parseResponse(String json) {
        List<MovieDTO> results = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode resultsNode = root.get("results");

            if (resultsNode != null && resultsNode.isArray()) {
                for (JsonNode node : resultsNode) {
                    MovieDTO movie = new MovieDTO();
                    movie.setId(node.get("id").asInt());
                    movie.setTitle(node.get("title").asText());
                    movie.setOverview(node.get("overview").asText(""));
                    movie.setReleaseDate(node.get("release_date").asText(""));
                    movie.setPosterPath(node.get("poster_path").asText(""));
                    movie.setRating(node.get("vote_average").asDouble(0.0));

                    results.add(movie);
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing TMDB JSON response", e);
        }
        return results;
    }

    /**
     * Searches for movies by title first in the local database,
     * and if none are found, queries TMDb and saves the results asynchronously.
     *
     * @param title The movie title to search for.
     * @return A list of MovieDTO objects representing the found movies.
     */
    @Override
    public List<MovieDTO> searchAndFallback(String title) {
        // 1. Search in DB with LIKE
        List<Movie> dbResults = movieRepository.findByTitleContainingIgnoreCase(title);
        if (!dbResults.isEmpty()) {
            return dbResults.stream()
                    .map(movieMapper::toDto)
                    .collect(Collectors.toList());
        }
        // 2. Search in TMDB
        List<MovieDTO> apiResults = searchMovies(title);

        if (apiResults.isEmpty()) {
            logger.info("No movies found locally or on TMDb for '{}'", title);
            return Collections.emptyList();
        }
        //3. Save Async in DB
        for (MovieDTO apiResult : apiResults) {
            asyncSaverService.saveMovieAndAvailability(apiResult);
        }
        //4.Return results to show in front
        return apiResults;
    }

    /**
     * Retrieves the list of streaming platforms available for a given TMDb movie ID.
     *
     * @param tmdbId The TMDb ID of the movie.
     * @return A list of AvailabilityDTO objects representing the platforms
     *         and countries where the movie is available.
     */
    public List<AvailabilityDTO> getProvidersForMovieId(int tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "/watch/providers";
        ResponseEntity<String> response = headerCreater(url);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("TMDb providers API failed for movieId {}: {}", tmdbId, response.getStatusCode());
            return Collections.emptyList();
        }
        List<AvailabilityDTO> availabilities = new ArrayList<>();

        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            JsonNode resultsNode = root.path("results");

            Iterator<String> countryCodes = resultsNode.fieldNames();
            while (countryCodes.hasNext()) {
                String countryCode = countryCodes.next();
                JsonNode countryNode = resultsNode.get(countryCode);
                // Search the country in DB by ISOCode
                Optional<Country> countryOpt = countryService.findByIsoCode(countryCode);

                if (countryOpt.isEmpty()) {
                    logger.warn("Country with ISO code {} not found in DB", countryCode);
                    continue;
                }

                if (!countryNode.has("flatrate")) {
                    logger.info("No flatrate providers for country {}", countryCode);
                    continue;
                }

                CountryDTO countryDTO = CountryMapper.toDto(countryOpt.get());
                for (JsonNode provider : countryNode.get("flatrate")) {
                    AvailabilityDTO availabilityDTO = new AvailabilityDTO();
                    // Search the platform in DB by Id
                    Platform platform = platformService.findById(provider.get("provider_id").asInt()).orElse(null);
                    if (platform != null) {
                        PlatformDTO platformDTO = PlatformMapper.toDto(platform);
                        availabilityDTO.setPlatform(platformDTO);
                    } else {
                        continue;
                    }
                    availabilityDTO.setCountry(countryDTO);
                    availabilities.add(availabilityDTO);
                }
            }


        } catch (Exception e) {
            logger.error("Error parsing availability providers for movieId {}", tmdbId, e);
        }
        return availabilities;
    }

    /**
     * Saves the availability (platform and country) data for a movie in the database.
     *
     * @param availabilityDTOs A list of AvailabilityDTO objects to save.
     * @param movie The Movie entity associated with the availability data.
     */
    public void saveAvailabilityData(List<AvailabilityDTO> availabilityDTOs, Movie movie) {
        if (availabilityDTOs == null || availabilityDTOs.isEmpty() || movie == null) {
            return;
        }
        List<Availability> availabilities = new ArrayList<>();
        for (AvailabilityDTO dto : availabilityDTOs) {
            // Buscar plataforma por Id
            Platform platform = platformService.findById(dto.getPlatform().getId())
                    .orElse(null);
            // Buscar país por ISO (por si no lo hiciste ya en el DTO)
            Country country = countryService.findByIsoCode(dto.getCountry().getIsoCode())
                    .orElse(null);
            if (country != null && platform != null) {
                Availability availability = AvailabilityMapper.toEntity(dto, movie);
                availability.setPlatform(platform);
                availability.setCountry(country);
                availabilities.add(availability);
            }
        }
        availabilityRepository.saveAll(availabilities);
    }

}

