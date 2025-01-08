package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.projection.PostDistanceProjection;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDistanceMapper implements EntityMapper<PostDistanceProjection, PostDto> {

    private final ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        modelMapper.addMappings(new PropertyMap<PostDistanceProjection, PostDto>() {
            @Override
            protected void configure() {
                map().getDetails().setDescription(source.getDescription());
                map().getDetails().setAvailableFrom(source.getFromDate());
                map().getDetails().setAvailableTo(source.getToDate());
                map().getDetails().setPrice(source.getPrice());

                map().getLocation().setCity(source.getCity());
                map().getLocation().setCountry(source.getCountry());
                map().getLocation().setState(source.getState());
            }
        });
    }

    @Override
    public PostDto toDto(PostDistanceProjection postDistanceProjection) {
        return modelMapper.map(postDistanceProjection, PostDto.class);
    }

    @Override
    public PostDistanceProjection toEntity(PostDto postDto) {
        return modelMapper.map(postDto, PostDistanceProjection.class);
    }
}
