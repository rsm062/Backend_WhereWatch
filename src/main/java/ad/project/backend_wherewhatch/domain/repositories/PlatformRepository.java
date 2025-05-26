package ad.project.backend_wherewhatch.domain.repositories;

import ad.project.backend_wherewhatch.domain.models.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Integer> {
}
