package ad.project.backend_wherewatch.domain.models;

import jakarta.persistence.*;


@Entity
@Table(name = "plataforms")
public class Platform {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String logoPath;

    public Platform() {
    }

    public Platform(int i, String name, String logoPath) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
