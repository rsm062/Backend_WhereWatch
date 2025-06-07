package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.InterfaceAsyncSaverService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Asynchronous service for saving movie data along with its availability.
 * This service fetches additional availability data using TMDb and saves it in the database.
 */
@Service
public class AsyncSaverService  implements InterfaceAsyncSaverService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncSaverService.class);

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final TmdbService tmdbService;

    @Autowired
    public AsyncSaverService(MovieRepository movieRepository, MovieMapper movieMapper, @Lazy TmdbService tmdbService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.tmdbService = tmdbService;
    }


    /**
     * Saves the movie and its availability data asynchronously.
     * Fetches availability using the TMDb service and saves both the movie and its availability.
     *
     * @param dto the movie DTO to process
     */
    @Override @Async @Transactional
    public void saveMovieAndAvailability(MovieDTO dto) {
        if(dto == null) {
            logger.warn("Attempted to save null MovieDTO");
            return;
        }
        try {
            int tmdbId = dto.getId();
            List<AvailabilityDTO> availabilities = tmdbService.getProvidersForMovieId(tmdbId);
            Movie movie = movieMapper.toEntity(dto);
            movie.setId(tmdbId);
            movieRepository.save(movie);
            tmdbService.saveAvailabilityData(availabilities, movie);
        } catch (Exception e) {
            logger.error("Failed to save movie and availability for DTO: {}", dto, e);
        }
    }
}
