package bth.postservice.mapper;

import bth.common.dto.PostDto;
import bth.postservice.config.MapperConfig;
import bth.postservice.entity.Post;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {PostMapper.class, MapperConfig.class})
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    @Disabled
    void givenToEntity_whenDtoFieldsAreSet_thenConvertToEntity() {
        // given
        var dto = EnhancedRandom.random(PostDto.class);
        // when
        var entity = postMapper.toEntity(dto);
        // then
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(dto.getUserId(), entity.getUserId());
    }

    @Test
    void givenToEntity_whenDtoFieldsAreNull_thenConvertToEntity() {
        // given
        var dto = new PostDto();
        // when
        var entity = postMapper.toEntity(dto);
        // then
        Assertions.assertNotNull(entity);
    }

    @Test
    void givenToDto_whenEntityFieldsAreSet_thenConvertToDto() {
        // given
        var entity = EnhancedRandom.random(Post.class, "location.locationPoint");
        // when
        var dto = postMapper.toDto(entity);
        // then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(entity.getUserId(), dto.getUserId());
        Assertions.assertEquals(entity.getLocation().getCity().getName(), dto.getLocation().getCity());
        Assertions.assertEquals(entity.getLocation().getCity().getCountry().getName(), dto.getLocation().getCountry());
    }

    @Test
    void givenToDto_whenEntityFieldsAreNull_thenConvertToDto() {
        // given
        var entity = new Post();
        // when
        var dto = postMapper.toDto(entity);
        // then
        Assertions.assertNotNull(dto);
    }
}