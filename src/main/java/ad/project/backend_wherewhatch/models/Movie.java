package ad.project.backend_wherewhatch.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "movies") @Data
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private Double rating;
    @ManyToMany
    private List<Platform> platforms;
    @ManyToMany
    private List<Country> countries;
}
