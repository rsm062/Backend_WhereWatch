package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.exception.MovieNotFoundException;
import ad.project.backend_wherewatch.application.service.InterfaceMovieService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling Movie entities.
 * Provides methods to find, save, and search movies.
 */
@Service
public class MovieService implements InterfaceMovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Finds a Movie by its exact title.
     *
     * @param title the exact title of the movie
     * @return an Optional containing the Movie if found, or empty otherwise
     */
    @Override
    public Optional<Movie> findByTitle(String title) {
        if (title == null || title.isBlank()) {
            logger.debug("findByTitle called with null or blank title");
            return Optional.empty();
        }
        try {
            return movieRepository.findByTitle(title);
        } catch (Exception e) {
            logger.error("Error finding movie by title: {}", title, e);
            throw e;
        }
    }

    /**
     * Saves a Movie entity to the database.
     *
     * @param movie the Movie entity to save
     * @return the saved Movie entity
     */
    @Override
    public Movie save(Movie movie) {
        if (movie == null) {
            logger.warn("Attempted to save null Movie");
            return null;
        }
        try {
            Movie saved = movieRepository.save(movie);
            logger.info("Saved Movie with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving movie: {}", movie, e);
            throw e;
        }
    }

    /**
     * Searches movies whose titles contain the given text (case-insensitive).
     *
     * @param title the partial title text to search
     * @return list of matching movies
     */
    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        if (title == null || title.isBlank()) {
            logger.debug("searchMoviesByTitle called with null or blank title");
            return List.of();
        }
        try {
            return movieRepository.findByTitleContainingIgnoreCase(title);
        } catch (Exception e) {
            logger.error("Error searching movies by title containing: {}", title, e);
            throw e;
        }
    }

    /**
     * Searches for a Movie by its ID.
     *
     * @param id the movie's ID
     * @return the Movie if found
     * @throws MovieNotFoundException if no Movie with the given ID is found
     */
    @Override
    public Movie searchMovieById(int id){
        try {
            return movieRepository.findById(id)
                    .orElseThrow(() -> new MovieNotFoundException(id));
        } catch (Exception e) {
            logger.error("Error searching movie by ID: {}", id, e);
            throw e;
        }
    }

}
