package ad.project.backend_wherewhatch.domain.repositories;

import ad.project.backend_wherewhatch.domain.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
}
