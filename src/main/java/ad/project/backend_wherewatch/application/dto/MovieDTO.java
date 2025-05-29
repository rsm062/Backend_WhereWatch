package ad.project.backend_wherewatch.application.dto;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieDTO {
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    /*private String posterPath;
    private Double rating;*/
    private List<AvailabilityDTO> availabilities = new ArrayList<>();

    public MovieDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /*public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }*/

    public List<AvailabilityDTO> getAvailabilities() {
        return availabilities == null ? Collections.emptyList() : availabilities;
    }

    public void setAvailabilities(List<AvailabilityDTO> availabilities) {
        this.availabilities = availabilities;
    }
}
