package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.MovieDTO;

import java.util.List;

/**
 * Service interface for interacting with the TMDb (The Movie Database) API.
 */
public interface InterfaceTmdbService {

    /**
     * Searches for movies by title using the TMDb API.
     *
     * @param title the title to search for
     * @return a list of matching {@link MovieDTO}s
     */
    List<MovieDTO> searchMovies(String title);

    /**
     * Searches for movies, save in database and provides a fallback if no results are found.
     *
     * @param title the title to search for
     * @return a list of matching {@link MovieDTO}s, possibly from a fallback source
     */
    List<MovieDTO> searchAndFallback(String title);
}
