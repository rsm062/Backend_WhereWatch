package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface InterfaceTmdbService {
    List<MovieDTO> searchMovies(String title);
    List<MovieDTO> searchAndFallback(String title);
}
