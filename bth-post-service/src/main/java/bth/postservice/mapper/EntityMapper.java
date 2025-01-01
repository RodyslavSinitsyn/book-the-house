package bth.postservice.mapper;

public interface EntityMapper<ENTITY, DTO> {
    DTO toDto(ENTITY entity);
    ENTITY toEntity(DTO dto);
}
