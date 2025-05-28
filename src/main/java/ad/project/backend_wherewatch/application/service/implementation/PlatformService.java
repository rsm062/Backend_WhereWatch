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

    @Autowired
    private PlatformRepository platformRepository;

    @Override
    public Optional<Platform> findByName(String name) {
        return platformRepository.findByName(name);
    }

    @Override
    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }
}
