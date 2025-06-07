package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.AvailabilityDTO;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.models.Movie;

/**
 * Mapper class for converting between {@link Availability} entities and {@link AvailabilityDTO} objects.
 */
public class AvailabilityMapper {
    /**
     * Converts an {@link Availability} entity to a {@link AvailabilityDTO}.
     *
     * @param availability the entity to convert
     * @return the corresponding DTO
     */
    public static AvailabilityDTO toDto(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setId(availability.getId());
        dto.setPlatform(PlatformMapper.toDto(availability.getPlatform()));
        dto.setCountry(CountryMapper.toDto(availability.getCountry()));
        return dto;
    }
    /**
     * Converts an {@link AvailabilityDTO} to an {@link Availability} entity, linking it to a given {@link Movie}.
     *
     * @param dto   the DTO to convert
     * @param movie the movie entity to associate with the availability
     * @return the corresponding entity
     */
    public static Availability toEntity(AvailabilityDTO dto, Movie movie) {
        Availability availability = new Availability();
        availability.setMovie(movie);
        availability.setPlatform(PlatformMapper.toEntity(dto.getPlatform()));
        availability.setCountry(CountryMapper.toEntity(dto.getCountry()));
        return availability;
    }

}