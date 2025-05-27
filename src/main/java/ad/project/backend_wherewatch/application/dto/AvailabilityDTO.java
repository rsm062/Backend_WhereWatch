package ad.project.backend_wherewatch.application.dto;


public class AvailabilityDTO {
    private Long id;
    private PlatformDTO platform;
    private CountryDTO country;

    public AvailabilityDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlatformDTO getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDTO platform) {
        this.platform = platform;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }
}
