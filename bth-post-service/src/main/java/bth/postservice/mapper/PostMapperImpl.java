package bth.postservice.mapper;

import bth.models.dto.PostDto;
import bth.postservice.entity.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostMapperImpl implements PostMapper {

    private static final ModelMapper MAPPER = new ModelMapper();

    @Override
    public Post toEntity(PostDto dto) {
        return MAPPER.map(dto, Post.class);
    }

    @Override
    public PostDto toDto(Post entity) {
        return MAPPER.map(entity, PostDto.class);
    }
}
