package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Country;

import java.util.Optional;

/**
 * Service interface for managing {@link Country} entities.
 */
public interface InterfaceCountryService {
    /**
     * Retrieves a country by its ISO code.
     *
     * @param isoCode the ISO code of the country
     * @return an {@link Optional} containing the country if found
     */
    Optional<Country> findByIsoCode(String isoCode);

    /**
     * Persists the given country in the database.
     *
     * @param country the country entity to save
     * @return the saved country entity
     */
    Country save(Country country);
}
