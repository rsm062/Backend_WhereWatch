package ad.project.backend_wherewatch.application.service.implementation;

import ad.project.backend_wherewatch.application.service.InterfacePlatformService;
import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService implements InterfacePlatformService {

    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public void ensureExists(Platform platform) {
        if (!platformRepository.existsByName(platform.getName())) {
            platformRepository.save(platform);
        }
    }
}
