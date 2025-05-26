package ad.project.backend_wherewhatch.application.service;

import ad.project.backend_wherewhatch.domain.models.Country;
import ad.project.backend_wherewhatch.domain.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }
}
