package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.domain.models.Movie;

import java.util.List;
import java.util.Optional;

public interface InterfaceMovieService {
    Optional<Movie> findByTitle(String title);
    Movie save(Movie movie);
}
