package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.MovieDTO;

import java.util.List;

public interface InterfaceMovieService {
    List<MovieDTO> searchMovies(String title);
    MovieDTO saveMovie(MovieDTO movieDTO);
    List<MovieDTO> searchMoviesFromExternalApi(String title, String apiKey);
    List<MovieDTO> getMoviesByTitle(String title);
}
