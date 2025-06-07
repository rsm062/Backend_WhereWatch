package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Platform;

import java.util.Optional;

/**
 * Service interface for managing {@link Platform} entities.
 */
public interface InterfacePlatformService {
    /**
     * Finds a platform by its name.
     *
     * @param name the name of the platform
     * @return an {@link Optional} containing the platform if found
     */
    Optional<Platform> findByName(String name);

    /**
     * Finds a platform by its ID.
     *
     * @param id the ID of the platform
     * @return an {@link Optional} containing the platform if found
     */
    Optional<Platform> findById(int id);

    /**
     * Persists the given platform in the database.
     *
     * @param platform the platform entity to save
     * @return the saved platform entity
     */
    Platform save(Platform platform);
}
