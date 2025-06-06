package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;

import java.util.List;

public interface InterfaceAsyncSaverService {
    void saveMovieAndAvailability(MovieDTO dto);
}
