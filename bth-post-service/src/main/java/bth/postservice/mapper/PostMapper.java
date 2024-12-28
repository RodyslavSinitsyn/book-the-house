package bth.postservice.mapper;

import bth.postservice.entity.Post;
import bth.models.dto.PostDto;

public interface PostMapper {
    Post toEntity(PostDto dto);
    PostDto toDto(Post entity);
}
