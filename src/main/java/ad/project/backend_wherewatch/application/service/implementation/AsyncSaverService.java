package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.InterfaceAsyncSaverService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncSaverService  implements InterfaceAsyncSaverService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    @Lazy
    private TmdbService tmdbService;

    @Override @Async @Transactional
    public void saveMovieAndAvailability(MovieDTO dto) {
        if(dto == null) {return;}
        int tmdbId = dto.getId();
        List<AvailabilityDTO> availabilities = tmdbService.getProvidersForMovieId(tmdbId);
        Movie movie = movieMapper.toEntity(dto);
        movie.setId(tmdbId);
        movieRepository.save(movie);
        tmdbService.saveAvailabilityData(availabilities, movie);
    }
}
