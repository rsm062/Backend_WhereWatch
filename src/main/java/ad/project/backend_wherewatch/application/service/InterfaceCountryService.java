package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Country;

public interface InterfaceCountryService {
    void ensureExists(Country country);
}
