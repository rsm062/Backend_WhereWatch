package ad.project.backend_wherewatch.application.mapper;

import ad.project.backend_wherewatch.application.dto.PlatformDTO;
import ad.project.backend_wherewatch.domain.models.Platform;

/**
 * Mapper class for converting between {@link Platform} entities and {@link PlatformDTO} objects.
 */
public class PlatformMapper {
    /**
     * Converts a {@link Platform} entity to a {@link PlatformDTO}.
     *
     * @param platform the entity to convert
     * @return the corresponding DTO
     */
    public static PlatformDTO toDto(Platform platform) {
        PlatformDTO dto = new PlatformDTO();
        dto.setId(platform.getId());
        dto.setName(platform.getName());
        dto.setLogoPath(platform.getLogoPath());
        return dto;
    }

    /**
     * Converts a {@link PlatformDTO} to a {@link Platform} entity.
     *
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public static Platform toEntity(PlatformDTO dto) {
        Platform platform = new Platform();
        platform.setId(dto.getId());
        platform.setName(dto.getName());
        platform.setLogoPath(dto.getLogoPath());
        return platform;
    }
}
