package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Availability;

/**
 * Service interface for managing {@link Availability} entities.
 */
public interface InterfaceAvailabilityService {
    /**
     * Persists the given availability in the database.
     *
     * @param availability the availability entity to save
     * @return the saved availability entity
     */
    Availability save(Availability availability);
}
