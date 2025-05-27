package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.PlatformDTO;
import ad.project.backend_wherewatch.domain.models.Platform;

public class PlatformMapper {
    public static PlatformDTO toDto(Platform platform) {
        PlatformDTO dto = new PlatformDTO();
        dto.setId(platform.getId());
        dto.setName(platform.getName());
        dto.setLogoPath(platform.getLogoPath());
        return dto;
    }

    public static Platform toEntity(PlatformDTO dto) {
        Platform platform = new Platform();
        platform.setId(dto.getId());
        platform.setName(dto.getName());
        platform.setLogoPath(dto.getLogoPath());
        return platform;
    }
}
