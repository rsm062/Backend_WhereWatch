package ad.project.backend_wherewatch.infrastructure.rest.controller;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.exception.MovieNotFoundException;
import ad.project.backend_wherewatch.application.service.implementation.TmdbService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.application.service.implementation.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages endpoints related to movies.
 * <p>
 * Supports searching movies by title and retrieving movie details by ID.
 */
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final TmdbService tmdbService;
    private final MovieService movieService;


    @Autowired
    public MovieController(TmdbService tmdbService, MovieService movieService) {
        this.tmdbService = tmdbService;
        this.movieService = movieService;
    }

    /**
     * Searches for movies by title.
     * <p>
     * First attempts to find movies locally in the database,
     * then falls back to the TMDb API if no local results are found.
     *
     * @param title the movie title to search for
     * @return a ResponseEntity containing a list of MovieDTOs matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String title) {
        List<MovieDTO> results = tmdbService.searchAndFallback(title);
        return ResponseEntity.ok(results);
    }

    /**
     * Retrieves movie details by its ID.
     *
     * @param id the ID of the movie
     * @return a ResponseEntity containing the Movie object if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        try {
            Movie movie = movieService.searchMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (MovieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<String> handleMovieNotFound(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

