package ad.project.backend_wherewhatch.domain.repositories;

import ad.project.backend_wherewhatch.domain.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
}
