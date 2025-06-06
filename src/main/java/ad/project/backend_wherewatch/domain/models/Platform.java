package ad.project.backend_wherewatch.domain.models;

import jakarta.persistence.*;


@Entity
@Table(name = "plataforms")
public class Platform {
    @Id
    private int id;
    @Column(unique = true)
    private String name;
    private String logoPath;

    public Platform() {
    }

    public Platform(int id, String name, String logoPath) {
        this.id = id;
        this.name = name;
        this.logoPath = logoPath;
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
