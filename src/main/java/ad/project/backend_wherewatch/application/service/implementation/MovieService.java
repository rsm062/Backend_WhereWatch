package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.InterfaceMovieService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import ad.project.backend_wherewatch.infrastructure.tmdb.TmdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService implements InterfaceMovieService {

    private final MovieRepository movieRepository;
    private final PlatformService platformService;
    private final CountryService countryService;
    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, PlatformService platformService, CountryService countryService, TmdbClient tmdbClient, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.platformService = platformService;
        this.countryService = countryService;
        this.tmdbClient = tmdbClient;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDTO> searchMovies(String title) {
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .map(MovieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO saveMovie(MovieDTO movieDTO) {
        Movie movie = MovieMapper.toEntity(movieDTO);

        // Guardar plataformas y países si no existen
        movie.getAvailabilities().forEach(av -> {
            platformService.ensureExists(av.getPlatform());
            countryService.ensureExists(av.getCountry());
        });

        Movie saved = movieRepository.save(movie);
        return MovieMapper.toDto(saved);
    }

    @Override
    public List<MovieDTO> searchMoviesFromExternalApi(String title, String apiKey) {
        try {
            List<MovieDTO> externalMovies = tmdbClient.searchMovies(title, apiKey);
            // Guardamos en BD las películas que no existan ya (por título)
            for (MovieDTO dto : externalMovies) {
                if (!movieRepository.existsByTitle(dto.getTitle())) {
                    saveMovie(dto);
                }
            }
            return externalMovies;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al llamar a TMDB", e);
        }
    }
    public List<MovieDTO> getMoviesByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
        List<MovieDTO> movieDTOs = new ArrayList<>();

        for (Movie movie : movies) {
            // Asumiendo que movieMapper es una instancia de tu mapper, no método estático
            MovieDTO dto = movieMapper.toDto(movie);
            movieDTOs.add(dto);
        }

        return movieDTOs;
    }
}
