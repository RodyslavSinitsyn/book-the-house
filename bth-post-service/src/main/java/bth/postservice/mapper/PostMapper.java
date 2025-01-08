package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.entity.Post;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper implements EntityMapper<Post, PostDto> {

    private final ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        modelMapper.addMappings(new PropertyMap<Post, PostDto>() {
            @Override
            protected void configure() {
                map().getLocation().setCity(source.getLocation().getCity().getName());
                map().getLocation().setCountry(source.getLocation().getCity().getCountry().getName());
                map().getLocation().setState(source.getLocation().getCity().getState().getName());

                map().setUsername(source.getUser().getUsername());
                map().setFriendlyName(source.getUser().getFriendlyName());
            }
        });
    }

    @Override
    public Post toEntity(PostDto dto) {
        return modelMapper.map(dto, Post.class);
    }

    @Override
    public PostDto toDto(Post entity) {
        return modelMapper.map(entity, PostDto.class);
    }
}
