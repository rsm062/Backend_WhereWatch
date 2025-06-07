package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Movie;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link Movie} entities.
 */
public interface InterfaceMovieService {

    /**
     * Finds a movie by its exact title.
     *
     * @param title the title of the movie
     * @return an {@link Optional} containing the movie if found
     */
    Optional<Movie> findByTitle(String title);

    /**
     * Persists the given movie in the database.
     *
     * @param movie the movie entity to save
     * @return the saved movie entity
     */
    Movie save(Movie movie);

    /**
     * Searches for movies containing the given title (case-insensitive).
     *
     * @param title the title fragment to search for
     * @return a list of matching movies
     */
    List<Movie> searchMoviesByTitle(String title);

    /**
     * Searches for a movie by its ID.
     *
     * @param id the ID of the movie
     * @return the movie if found, or throws an exception
     */
    Movie searchMovieById(int id);

}
