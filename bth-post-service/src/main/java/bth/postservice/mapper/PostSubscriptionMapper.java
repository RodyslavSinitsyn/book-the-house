package bth.postservice.mapper;

import bth.common.dto.PostSubscriptionDto;
import bth.postservice.entity.PostSubscription;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSubscriptionMapper implements EntityMapper<PostSubscription, PostSubscriptionDto> {

    private final ModelMapper modelMapper;

    @Override
    public PostSubscriptionDto toDto(PostSubscription postSubscription) {
        return modelMapper.map(postSubscription, PostSubscriptionDto.class);
    }

    @Override
    public PostSubscription toEntity(PostSubscriptionDto postSubscriptionDto) {
        return modelMapper.map(postSubscriptionDto, PostSubscription.class);
    }
}
