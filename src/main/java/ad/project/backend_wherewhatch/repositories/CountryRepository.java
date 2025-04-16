package ad.project.backend_wherewhatch.repositories;

import ad.project.backend_wherewhatch.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
