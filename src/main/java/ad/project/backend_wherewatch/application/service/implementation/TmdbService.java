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

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;
    private final AsyncSaverService asyncSaverService;
    private final PlatformService platformService;
    private final CountryService countryService;
    private final MovieMapper movieMapper;
    private final AvailabilityRepository availabilityRepository;




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

    @Override
    public List<MovieDTO> searchMovies(String title) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("query", UriUtils.encode(title, StandardCharsets.UTF_8))
                //.queryParam("include_adult", true)
                //.queryParam("language", "en-US")
                .queryParam("page", 1)
                .toUriString();

        ResponseEntity<String> response = headerCreater(url);

        // Parsear JSON a lista de MovieDTO
        return parseResponse(response.getBody());
    }

    private ResponseEntity<String> headerCreater(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
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
                    movie.setPosterPath(node.get("poster_path").asText(""));
                    movie.setRating(node.get("vote_average").asDouble(0.0));

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
        for (MovieDTO apiResult : apiResults) {
            asyncSaverService.saveMovieAndAvailability(apiResult);
        }

        /*//  Convertir a entidades y guardar en BBDD
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

        }*/

        return apiResults;
    }

    public List<AvailabilityDTO> getProvidersForMovieId(int tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "/watch/providers";

        ResponseEntity<String> response = headerCreater(url);

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
                        AvailabilityDTO availabilityDTO = new AvailabilityDTO();
                        // Construir PlatformDTO
                        Platform platform = platformService.findById(provider.get("provider_id").asInt()).orElse(null);
                        if (platform != null) {
                            PlatformDTO platformDTO = PlatformMapper.toDto(platform);
                            availabilityDTO.setPlatform(platformDTO);
                        }else{
                            continue;
                        }
                        availabilityDTO.setCountry(countryDTO);
                        availabilities.add(availabilityDTO);
                    }
                } else {
                    System.err.println("No se encontró el país con ISO: " + countryCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return availabilities;
    }

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

