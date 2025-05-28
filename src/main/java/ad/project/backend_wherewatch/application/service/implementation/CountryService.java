package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfaceCountryService;
import ad.project.backend_wherewatch.domain.models.Country;
import ad.project.backend_wherewatch.domain.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService implements InterfaceCountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Optional<Country> findByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode);
    }

    @Override
    public Country save(Country country) {
        return countryRepository.save(country);
    }
}
