package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.models.Movie;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    public MovieDTO toDto(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setOverview(movie.getOverview());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setPosterPath(movie.getPosterPath());
        dto.setRating(movie.getRating());
        dto.setAvailabilities(
                movie.getAvailabilities().stream()
                        .map(AvailabilityMapper::toDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setId(dto.getId());
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setPosterPath(dto.getPosterPath());
        movie.setRating(dto.getRating());

        List<AvailabilityDTO> availabilities = dto.getAvailabilities();
        if (availabilities == null) {
            availabilities = Collections.emptyList();
        }
        movie.setAvailabilities(
            availabilities.stream()
                    .map(dtoItem -> AvailabilityMapper.toEntity(dtoItem, movie))
                    .collect(Collectors.toList())
        );
        return movie;
    }
}
