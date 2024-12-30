package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.entity.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper implements EntityMapper<Post, PostDto> {

    private final ModelMapper modelMapper;

    @Override
    public Post toEntity(PostDto dto) {
        return modelMapper.map(dto, Post.class);
    }

    @Override
    public PostDto toDto(Post entity) {
        return modelMapper.map(entity, PostDto.class);
    }
}
