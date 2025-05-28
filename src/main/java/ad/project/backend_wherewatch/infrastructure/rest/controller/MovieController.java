package ad.project.backend_wherewatch.infrastructure.rest.controller;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.service.InterfaceMovieService;
import ad.project.backend_wherewatch.application.service.implementation.TmdbService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.application.service.implementation.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private final TmdbService tmdbService;

    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // Buscar películas por título, guardar en BBDD y devolver lista
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMoviesByTitle(@RequestParam String title) {
        try {
            List<MovieDTO> movies = tmdbService.searchAndSaveMoviesByTitle(title);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

