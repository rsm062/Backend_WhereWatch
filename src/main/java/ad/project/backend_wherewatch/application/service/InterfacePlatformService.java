package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Platform;

import java.util.Optional;

public interface InterfacePlatformService {
    Optional<Platform> findByName(String name);
    Platform save(Platform platform);
}
