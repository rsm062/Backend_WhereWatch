package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfaceCountryService;
import ad.project.backend_wherewatch.domain.models.Country;
import ad.project.backend_wherewatch.domain.repositories.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling Country entities.
 * Provides methods to find and save countries.
 */
@Service
public class CountryService implements InterfaceCountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Finds a Country by its ISO code.
     *
     * @param isoCode the ISO code of the country
     * @return an Optional containing the Country if found, or empty otherwise
     */
    @Override
    public Optional<Country> findByIsoCode(String isoCode) {
        if (isoCode == null || isoCode.isBlank()) {
            logger.warn("ISO code is null or blank");
            return Optional.empty();
        }
        try {
            return countryRepository.findByIsoCode(isoCode);
        } catch (Exception e) {
            logger.error("Failed to find Country by ISO code: {}", isoCode, e);
            throw e;
        }
    }

    /**
     * Saves a Country entity to the database.
     *
     * @param country the Country to save
     * @return the saved Country
     */
    @Override
    public Country save(Country country) {
        if (country == null) {
            logger.warn("Attempted to save a null Country");
            return null;
        }

        try {
            Country saved = countryRepository.save(country);
            logger.info("Saved Country with ID: {} and ISO code: {}", saved.getId(), saved.getIsoCode());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving Country: {}", country, e);
            throw e;
        }
    }
}
