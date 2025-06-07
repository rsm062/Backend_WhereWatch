package ad.project.backend_wherewatch.infrastructure.init;

import ad.project.backend_wherewatch.application.service.implementation.CountryService;
import ad.project.backend_wherewatch.domain.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes the predefined list of countries in the database at application startup.
 * <p>
 * Implements CommandLineRunner to run this initialization logic automatically
 * when the Spring Boot application starts.
 */
@Component
public class CountryInit implements CommandLineRunner {

    private final CountryService countryService;

    @Autowired
    public CountryInit(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Runs on application startup and ensures that the predefined list of countries
     * exists in the database. If a country with the same ISO code is already present,
     * it will not be added again.
     *
     * @param args Command line arguments (not used).
     */
    @Override
    public void run(String... args) {
        List<Country> countries = Arrays.asList(
                new Country("Argentina", "AR"),
                new Country("Australia", "AU"),
                new Country("Austria", "AT"),
                new Country("Bélgica", "BE"),
                new Country("Brasil", "BR"),
                new Country("Canadá", "CA"),
                new Country("Chile", "CL"),
                new Country("Dinamarca", "DK"),
                new Country("Francia", "FR"),
                new Country("Alemania", "DE"),
                new Country("India", "IN"),
                new Country("Italia", "IT"),
                new Country("Japón", "JP"),
                new Country("México", "MX"),
                new Country("Países Bajos", "NL"),
                new Country("Noruega", "NO"),
                new Country("Polonia", "PL"),
                new Country("Portugal", "PT"),
                new Country("Corea del Sur", "KR"),
                new Country("España", "ES"),
                new Country("Suecia", "SE"),
                new Country("Reino Unido", "GB"),
                new Country("Estados Unidos", "US")
        );

        for (Country country : countries) {
            countryService.findByIsoCode(country.getIsoCode())
                    .orElseGet(() -> countryService.save(country));
        }
    }
}
