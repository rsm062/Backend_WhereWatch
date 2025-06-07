package ad.project.backend_wherewatch.application.exception;

/**
 * Exception thrown when a Movie with a specified ID is not found.
 */
public class MovieNotFoundException extends RuntimeException {
    /**
     * Constructs a new MovieNotFoundException with a detailed message.
     *
     * @param id the ID of the movie that was not found
     */
    public MovieNotFoundException(int id) {
        super("Movie with ID " + id + " not found");
    }
}

