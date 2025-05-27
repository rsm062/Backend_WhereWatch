package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.CountryDTO;
import ad.project.backend_wherewatch.domain.models.Country;

public class CountryMapper {
    public static CountryDTO toDto(Country country) {
        CountryDTO dto = new CountryDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setIsoCode(country.getIsoCode());
        return dto;
    }

    public static Country toEntity(CountryDTO dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        country.setIsoCode(dto.getIsoCode());
        return country;
    }
}
