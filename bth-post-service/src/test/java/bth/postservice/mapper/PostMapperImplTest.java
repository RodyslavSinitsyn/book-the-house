package bth.postservice.mapper;

import bth.models.dto.PostDto;
import bth.postservice.entity.Post;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PostMapperImplTest {

    private final PostMapper postMapper = new PostMapperImpl();

    @Test
    void givenToEntity_whenDtoFieldsAreSet_thenConvertToEntity() {
        // given
        var dto = EnhancedRandom.random(PostDto.class);
        // when
        var entity = postMapper.toEntity(dto);
        // then
        Assertions.assertNotNull(entity);
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