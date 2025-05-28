package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfaceAvailabilityService;
import ad.project.backend_wherewatch.domain.models.Availability;
import ad.project.backend_wherewatch.domain.repositories.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService implements InterfaceAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Override
    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }
}
