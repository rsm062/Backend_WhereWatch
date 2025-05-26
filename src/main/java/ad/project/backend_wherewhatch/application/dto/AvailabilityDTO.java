package ad.project.backend_wherewhatch.application.dto;

import lombok.Data;

@Data
public class AvailabilityDTO {
    private Long id;
    private PlatformDTO platform;
    private CountryDTO country;
}
