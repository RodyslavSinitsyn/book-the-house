package bth.postservice.repo.jpa;

import bth.postservice.entity.PostSubscription;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: Not working when @EnableElasticsearchRepositories("bth.postservice.repo.elastic") exists
@Disabled
class PostSubscriptionRepositoryTest extends AbstractJpaTest {

    @Autowired
    private PostSubscriptionRepository repository;

    @Test
    void test() {
        var save = repository.save(EnhancedRandom.random(PostSubscription.class, "id"));

        var count = repository.count();

        Assertions.assertEquals(1, count);
    }
}