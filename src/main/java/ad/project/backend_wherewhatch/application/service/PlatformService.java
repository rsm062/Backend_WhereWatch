package ad.project.backend_wherewhatch.application.service;

import ad.project.backend_wherewhatch.domain.models.Platform;
import ad.project.backend_wherewhatch.domain.repositories.PlatformRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    public Platform savePlatform(Platform platform) {
        return platformRepository.save(platform);
    }

    public Optional<Platform> getPlatformById(int id) {
        return platformRepository.findById(id);
    }
}