package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.MovieDTO;

/**
 * Service interface for asynchronously saving movie data along with its availability.
 */
public interface InterfaceAsyncSaverService {
    /**
     * Saves a movie and its availability information.
     *
     * @param dto the movie data to save
     */
    void saveMovieAndAvailability(MovieDTO dto);
}
