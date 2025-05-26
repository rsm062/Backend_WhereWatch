package ad.project.backend_wherewhatch.infrastructure.rest.controller;

import ad.project.backend_wherewhatch.application.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchMovie(@RequestParam String query) {
        String result = tmdbService.searchMovie(query);
        return ResponseEntity.ok(result);
    }
}
