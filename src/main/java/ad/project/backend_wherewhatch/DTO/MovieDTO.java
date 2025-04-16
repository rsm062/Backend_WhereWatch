package ad.project.backend_wherewhatch.DTO;

import lombok.Data;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
}
