package ad.project.backend_wherewatch.domain.repositories;

import ad.project.backend_wherewatch.domain.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Country} entities.
 * Extends {@link JpaRepository} and provides a method to find a country by its ISO code.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    /**
     * Finds a country by its ISO code.
     *
     * @param isoCode the ISO code of the country
     * @return an {@link Optional} containing the country if found, or empty otherwise
     */
    Optional<Country> findByIsoCode(String isoCode);
}
