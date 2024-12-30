package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.entity.Post;

public interface PostMapper {
    Post toEntity(PostDto dto);
    PostDto toDto(Post entity);
}
