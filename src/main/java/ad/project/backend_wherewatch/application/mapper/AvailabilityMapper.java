package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.models.Movie;

public class AvailabilityMapper {
    public static AvailabilityDTO toDto(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setId(availability.getId());
        dto.setPlatform(PlatformMapper.toDto(availability.getPlatform()));
        dto.setCountry(CountryMapper.toDto(availability.getCountry()));
        return dto;
    }
    public static Availability toEntity(AvailabilityDTO dto, Movie movie) {
        Availability availability = new Availability();
        availability.setMovie(movie);
        availability.setPlatform(PlatformMapper.toEntity(dto.getPlatform()));
        availability.setCountry(CountryMapper.toEntity(dto.getCountry()));
        return availability;
    }

}