package ad.project.backend_wherewatch.application.service;

import ad.project.backend_wherewatch.domain.models.Platform;

public interface InterfacePlatformService {
    void ensureExists(Platform platform);
}
