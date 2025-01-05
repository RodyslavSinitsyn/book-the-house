package bth.postservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@Slf4j
class PostGeneratorServiceTest {

    private final PostGeneratorService postGeneratorService = new PostGeneratorService(null); // TODO: Spring boot test

    @Test
    void testGenerate() {
        var post = postGeneratorService.generate();
        assertNotNull(post);
        log.info(post.toString());
    }
}