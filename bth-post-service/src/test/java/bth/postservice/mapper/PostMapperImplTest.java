package bth.postservice.mapper;

import bth.models.dto.PostDto;
import bth.postservice.entity.Post;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostMapperImplTest {

    @Autowired
    private PostMapper postMapper;

    @Test
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
        var entity = new Post();
        // when
        var dto = postMapper.toDto(entity);
        // then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(entity.getUserId(), dto.getUserId());
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