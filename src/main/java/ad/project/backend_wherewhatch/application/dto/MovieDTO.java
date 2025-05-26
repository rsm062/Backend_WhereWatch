package ad.project.backend_wherewhatch.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private List<AvailabilityDTO> availabilities;
}
