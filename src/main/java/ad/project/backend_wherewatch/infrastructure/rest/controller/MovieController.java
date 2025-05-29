package ad.project.backend_wherewatch.infrastructure.rest.controller;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.implementation.TmdbService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.application.service.implementation.MovieService;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private final TmdbService tmdbService;

    @Autowired
    private final MovieService movieService;

    @Autowired
    private final MovieMapper movieMapper;
    @Autowired
    private MovieRepository movieRepository;

    public MovieController(TmdbService tmdbService, MovieService movieService, MovieMapper movieMapper) {
        this.tmdbService = tmdbService;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @GetMapping("/search")
    public List<MovieDTO> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
        return movies.stream()
                .map(MovieMapper::toDto)
                .collect(Collectors.toList());
    }

    /*@GetMapping("/search")
    public List<Movie> searchMoviesByTitle(@RequestParam String title) {
        return movieService.searchMoviesByTitle(title);
    }*/

   /* // Buscar películas por título, guardar en BBDD y devolver lista
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMoviesByTitle(@RequestParam String title) {
        try {
            List<MovieDTO> movies = tmdbService.searchAndSaveMoviesByTitle(title);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
}

