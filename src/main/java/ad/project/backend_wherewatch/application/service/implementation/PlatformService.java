package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfacePlatformService;
import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.PlatformRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing Platform entities.
 */
@Service
public class PlatformService implements InterfacePlatformService {

    private static final Logger logger = LoggerFactory.getLogger(PlatformService.class);


    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    /**
     * Finds a Platform by its name.
     *
     * @param name the name of the platform
     * @return an Optional containing the Platform if found, or empty otherwise
     */
    @Override
    public Optional<Platform> findByName(String name) {
        try {
            return platformRepository.findByName(name);
        } catch (Exception e) {
            logger.error("Error finding platform by name '{}': {}", name, e.getMessage(), e);
            return Optional.empty();
        }
    }


    /**
     * Finds a Platform by its ID.
     *
     * @param id the ID of the platform
     * @return an Optional containing the Platform if found, or empty otherwise
     */
    @Override
    public Optional<Platform> findById(int id) {
        try {
            return platformRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error finding platform by id '{}': {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }


    /**
     * Saves a Platform entity.
     *
     * @param platform the Platform entity to save
     * @return the saved Platform entity
     * @throws IllegalArgumentException if the platform with the given ID does not exist
     */
    @Override
    @Transactional
    public Platform save(Platform platform) {
        try {
            if (platform == null) {
                throw new IllegalArgumentException("Platform cannot be null");
            }
            return platformRepository.save(platform);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid platform entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving platform '{}': {}", platform != null ? platform.getName() : "null", e.getMessage(), e);
            throw new RuntimeException("Failed to save platform", e);
        }
    }

}
