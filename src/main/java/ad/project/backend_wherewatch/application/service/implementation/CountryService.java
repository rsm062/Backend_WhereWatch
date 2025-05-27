package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfaceCountryService;
import ad.project.backend_wherewatch.domain.models.Country;
import ad.project.backend_wherewatch.domain.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService implements InterfaceCountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void ensureExists(Country country) {
        if (!countryRepository.existsByIsoCode(country.getIsoCode())) {
            countryRepository.save(country);
        }
    }
}
