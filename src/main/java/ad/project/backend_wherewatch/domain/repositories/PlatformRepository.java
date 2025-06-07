package ad.project.backend_wherewatch.domain.repositories;

import ad.project.backend_wherewatch.domain.models.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Platform} entities.
 * Extends {@link JpaRepository} and provides a method to find a platform by name.
 */
@Repository
public interface PlatformRepository extends JpaRepository<Platform, Integer> {
    /**
     * Finds a platform by its name.
     *
     * @param name the name of the platform
     * @return an {@link Optional} containing the platform if found, or empty otherwise
     */
    Optional<Platform> findByName(String name);
}
