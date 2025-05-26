package ad.project.backend_wherewhatch.domain.repositories;

import ad.project.backend_wherewhatch.domain.models.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Integer> {
}
