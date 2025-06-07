package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.CountryDTO;
import ad.project.backend_wherewatch.domain.models.Country;

/**
 * Mapper class for converting between {@link Country} entities and {@link CountryDTO} objects.
 */
public class CountryMapper {

    /**
     * Converts a {@link Country} entity to a {@link CountryDTO}.
     *
     * @param country the entity to convert
     * @return the corresponding DTO
     */
    public static CountryDTO toDto(Country country) {
        CountryDTO dto = new CountryDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setIsoCode(country.getIsoCode());
        return dto;
    }

    /**
     * Converts a {@link CountryDTO} to a {@link Country} entity.
     *
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public static Country toEntity(CountryDTO dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        country.setIsoCode(dto.getIsoCode());
        return country;
    }
}
