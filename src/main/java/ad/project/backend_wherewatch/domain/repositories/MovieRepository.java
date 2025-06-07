package ad.project.backend_wherewatch.domain.repositories;

import ad.project.backend_wherewatch.domain.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Movie} entities.
 * Extends {@link JpaRepository} and provides additional methods to find movies by title or ID.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>{
    /**
     * Finds a movie by its exact title.
     *
     * @param title the title to search
     * @return an {@link Optional} containing the movie if found, or empty otherwise
     */
    Optional<Movie> findByTitle(String title);

    /**
     * Finds a list of movies that contain the specified title (case-insensitive).
     *
     * @param title the title fragment to search
     * @return a list of matching movies
     */
    List<Movie> findByTitleContainingIgnoreCase(String title);

}
