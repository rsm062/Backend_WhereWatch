package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Country;

import java.util.Optional;

public interface InterfaceCountryService {
    Optional<Country> findByIsoCode(String isoCode);
    Country save(Country country);
}
