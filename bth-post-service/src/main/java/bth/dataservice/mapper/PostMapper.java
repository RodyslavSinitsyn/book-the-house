package bth.dataservice.mapper;

import bth.dataservice.entity.Post;
import bth.models.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostDto dto);
    PostDto toDto(Post entity);
}
