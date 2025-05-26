package ad.project.backend_wherewhatch.domain.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "plataforms") @Data
public class Platform {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String logoPath;
}
