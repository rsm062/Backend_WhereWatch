package ad.project.backend_wherewatch.infrastructure.rest.controller;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.service.InterfaceMovieService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.application.service.implementation.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final InterfaceMovieService movieService;

    @Autowired
    public MovieController(InterfaceMovieService movieService) {
        this.movieService = movieService;
    }

    // GET /api/movies?title=matrix
    @GetMapping
    public List<MovieDTO> searchMovies(@RequestParam String title) {
        return movieService.searchMovies(title);
    }

    // POST /api/movies
    @PostMapping
    public MovieDTO saveMovie(@RequestBody MovieDTO movieDTO) {
        return movieService.saveMovie(movieDTO);
    }
    /*@GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "false") boolean external,
            @RequestParam(required = false) String apiKey) {
        System.err.println("API Key recibida: " + apiKey);
        List<MovieDTO> movies;
        if (external) {
            movies = movieService.searchMoviesFromExternalApi(title, apiKey);
        } else {
            movies = movieService.searchMovies(title);
        }
        return ResponseEntity.ok(movies);
    }*/
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> getMovies(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "false") boolean external,
            @RequestParam String apiKey) {

        if (external) {
            // Lógica para obtener de fuente externa y/o guardar
        }

        List<MovieDTO> movies = movieService.getMoviesByTitle(title);
        return ResponseEntity.ok(movies);
    }
}

