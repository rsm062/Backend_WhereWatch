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
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.models.Country;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.AvailabilityRepository;
import ad.project.backend_wherewatch.domain.repositories.CountryRepository;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TmdbService implements InterfaceTmdbService {


    private RestTemplate restTemplate;

   /* @Autowired
    private MovieService movieService;*/

    @Autowired
    private final MovieRepository movieRepository;


    @Autowired
    private PlatformService platformService;

    @Autowired
    private CountryService countryService;

    /*@Autowired
    private AvailabilityService availabilityService;*/

    @Autowired
    private MovieMapper movieMapper;

    /*@Autowired
    private final ObjectMapper objectMapper;*/

    private final String TMDB_API_KEY = "346172216d64b089ac0f2697e41ab7fa";
    private final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?query={title}&api_key={apiKey}";
    @Autowired
    private AvailabilityRepository availabilityRepository;


    public TmdbService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    @Value("${tmdb.api.key}")
    private String apiKey; // Token completo con 'Bearer ...'

    @Autowired
    public TmdbService(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> searchMovies(String title) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("query", title)
                //.queryParam("include_adult", true)
                //.queryParam("language", "en-US")
                .queryParam("page", 1)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        // Parsear JSON a lista de MovieDTO
        return parseResponse(response.getBody());
    }

    private List<MovieDTO> parseResponse(String json) {
        // Usa Jackson para parsear JSON
        ObjectMapper mapper = new ObjectMapper();
        List<MovieDTO> results = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(json);
            JsonNode resultsNode = root.get("results");

            if (resultsNode != null && resultsNode.isArray()) {
                for (JsonNode node : resultsNode) {
                    MovieDTO movie = new MovieDTO();
                    movie.setId(node.get("id").asInt());
                    movie.setTitle(node.get("title").asText());
                    movie.setOverview(node.get("overview").asText(""));
                    movie.setReleaseDate(node.get("release_date").asText(""));
                    /*movie.setPosterPath(node.get("poster_path").asText(""));
                    movie.setRating(node.get("vote_average").asDouble(0.0));*/

                    results.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    @Override
    public List<MovieDTO> searchAndFallback(String title) {
        // 1. Buscar en BBDD con LIKE
        List<Movie> dbResults = movieRepository.findByTitleContainingIgnoreCase(title);

        if (!dbResults.isEmpty()) {
            return dbResults.stream()
                    .map(movieMapper::toDto)
                    .collect(Collectors.toList());
        }

        // 2. Buscar en TMDB
        List<MovieDTO> apiResults = searchMovies(title);

        if (apiResults.isEmpty()) {
            return Collections.emptyList(); // Nada encontrado ni local ni remoto
        }
        List<MovieDTO> finalResults = new ArrayList<>();

        //  Convertir a entidades y guardar en BBDD
        for (MovieDTO dto : apiResults) {
            //Extraer id
            int tmdbId = dto.getId();
            // Obtener availabilities desde TMDB
            List<AvailabilityDTO> availabilities = getProvidersForMovieId(tmdbId);
            //Mapear a entidad y guardar Movie + Availability
            Movie movie = movieMapper.toEntity(dto);
            movie.setId(tmdbId);
            movieRepository.save(movie);
            //Guardamos disponibilidad
            saveAvailabilityData(availabilities, movie);
            //preparamos para devolver al front
            dto.setAvailabilities(availabilities);
            finalResults.add(dto);

        }

        return apiResults;
    }

    public List<AvailabilityDTO> getProvidersForMovieId(int tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "/watch/providers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        List<AvailabilityDTO> availabilities = new ArrayList<>();

        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            JsonNode resultsNode = root.path("results");

            Iterator<String> fieldNames = resultsNode.fieldNames();
            while (fieldNames.hasNext()) {
                String countryCode = fieldNames.next();
                JsonNode countryNode = resultsNode.get(countryCode);
                // Buscar el país en base de datos por ISO

                Optional<Country> countryOpt = countryService.findByIsoCode(countryCode);
                if (countryOpt.isPresent() && countryNode.has("flatrate")) {
                    CountryDTO countryDTO = CountryMapper.toDto(countryOpt.get());
                    for (JsonNode provider : countryNode.get("flatrate")) {
                        AvailabilityDTO dto = new AvailabilityDTO();
                        // Construir PlatformDTO
                        PlatformDTO platform = new PlatformDTO();
                        platform.setName(provider.get("provider_name").asText());
                        // Si tienes id o logo, lo puedes setear también aquí
                        // platform.setId(provider.get("provider_id").asLong());

                        // Construir CountryDTO

                        dto.setPlatform(platform);
                        dto.setCountry(countryDTO);

                        availabilities.add(dto);
                    }
                }else {
                    System.err.println("No se encontró el país con ISO: " + countryCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return availabilities;
    }

    @Autowired
    private AvailabilityService availabilityService;


    private AvailabilityMapper availabilityMapper;

    public void saveAvailabilityData(List<AvailabilityDTO> availabilityDTOs, Movie movie) {
        if (availabilityDTOs == null || availabilityDTOs.isEmpty() || movie == null) {
            return; // O puedes lanzar una excepción si lo prefieres
        }
        List<Availability> entities = new ArrayList<>();

        for (AvailabilityDTO dto : availabilityDTOs) {
            // Buscar plataforma por nombre
            Platform platform = platformService.findByName(dto.getPlatform().getName())
                    .orElseGet(() -> {
                        Platform newPlatform = PlatformMapper.toEntity(dto.getPlatform());
                        return platformService.save(newPlatform);
                    });

            // Buscar país por ISO (por si no lo hiciste ya en el DTO)
            Country country = countryService.findByIsoCode(dto.getCountry().getIsoCode())
                    .orElse(null); // O lanza excepción si prefieres

            if (country != null) {
                Availability availability = AvailabilityMapper.toEntity(dto, movie);
                availability.setPlatform(platform);
                availability.setCountry(country);
                entities.add(availability);
            }
        }
        availabilityRepository.saveAll(entities);
    }





}

