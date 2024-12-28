package bth.postservice.mapper;

import bth.models.dto.PostDto;
import bth.postservice.entity.Post;

public interface PostMapper {
    Post toEntity(PostDto dto);
    PostDto toDto(Post entity);
}
