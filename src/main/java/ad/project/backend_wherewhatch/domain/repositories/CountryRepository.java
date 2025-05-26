package ad.project.backend_wherewhatch.domain.repositories;

import ad.project.backend_wherewhatch.domain.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
