package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfaceAvailabilityService;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.repositories.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service implementation for handling Availability entities.
 * Provides operations for saving availability data to the database.
 */
@Service
public class AvailabilityService implements InterfaceAvailabilityService {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);


    private final AvailabilityRepository availabilityRepository;

    @Autowired
    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }


    /**
     * Saves an Availability entity to the database.
     *
     * @param availability the Availability entity to save
     * @return the saved Availability entity
     */
    @Override
    public Availability save(Availability availability) {
        if (availability == null) {
            logger.warn("Attempted to save a null Availability");
            return null;
        }

        try {
            Availability saved = availabilityRepository.save(availability);
            logger.info("Saved Availability with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Error while saving Availability: {}", availability, e);
            throw e; // Rethrow or handle as needed
        }
    }
}
