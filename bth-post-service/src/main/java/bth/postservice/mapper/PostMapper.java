package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.entity.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        var dto = modelMapper.map(entity, PostDto.class);
        Optional.ofNullable(entity.getLocation())
                .ifPresent(location -> {
                    dto.getLocation().setCity(location.getCity().getName());
                    dto.getLocation().setCountry(location.getCity().getCountry().getName());
                    dto.getLocation().setState(location.getCity().getState().getName());
                });
        return dto;
    }
}
