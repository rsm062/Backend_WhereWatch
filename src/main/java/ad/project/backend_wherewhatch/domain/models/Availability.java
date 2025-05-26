package ad.project.backend_wherewhatch.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Table(name = "availabilities") @Data
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}

