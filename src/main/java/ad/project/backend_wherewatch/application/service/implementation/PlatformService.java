package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfacePlatformService;
import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.PlatformRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlatformService implements InterfacePlatformService {

    private static final Logger logger = LoggerFactory.getLogger(PlatformService.class);
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private PlatformRepository platformRepository;

    @Override
    public Optional<Platform> findByName(String name) {
        return platformRepository.findByName(name);
    }
    @Override
    public Optional<Platform> findById(int id) {
        return platformRepository.findById(id);
    }


    @Override
    @Transactional
    public Platform save(Platform platform) {
        return platformRepository.findById(platform.getId())
                .orElseThrow(() -> new IllegalArgumentException("Platform not found: " + platform.getName()));
    }

}
