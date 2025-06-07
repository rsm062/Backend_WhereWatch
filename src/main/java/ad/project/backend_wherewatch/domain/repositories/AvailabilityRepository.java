package ad.project.backend_wherewatch.domain.repositories;

import ad.project.backend_wherewatch.domain.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Availability} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
