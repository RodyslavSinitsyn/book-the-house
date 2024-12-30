package bth.postservice.resolver;

import bth.common.contract.PostService;
import bth.common.dto.PostDto;
import bth.common.dto.filter.PostsFilterDto;
import bth.common.exception.PostNotFoundException;
import bth.common.rabbitmq.RabbitProperties;
import bth.common.rabbitmq.message.PostCreatedMessage;
import bth.postservice.entity.Post;
import bth.postservice.mapper.PostMapper;
import bth.postservice.repo.PostSubscriptionRepository;
import bth.postservice.repo.PostsRepository;
import bth.postservice.service.PostGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostResolver implements PostService {
    private static final int BATCH_SIZE = 6;

    private final PostsRepository postsRepository;
    private final PostSubscriptionRepository postSubscriptionRepository;
    private final PostMapper postMapper;
    private final PostGeneratorService postGeneratorService;
    private final AmqpTemplate amqpTemplate;
    private final RabbitProperties rabbitProperties;

    @Override
    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var postsPage = postsRepository.findFilteredPosts(filter, page, BATCH_SIZE);
        log.debug("Successfully loaded {} posts, total size: {}", postsPage.getNumberOfElements(), postsPage.getTotalElements());
        return postsPage.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    @QueryMapping
    public PostDto post(@Argument("id") String id) {
        return postMapper.toDto(
                postsRepository.findById(UUID.fromString(id)).orElseThrow(() -> new PostNotFoundException(id)));
    }

    @Override
    @MutationMapping
    public PostDto createPost(@Argument("imageUrl") String imageUrl,
                              @Argument("userId") String userId) {
        var post = postGeneratorService.generate();
        post.setUserId(userId);
        post.setImageUrl(imageUrl);
        var savedPost = postsRepository.save(post);
        notifySubscribers(savedPost);
        return postMapper.toDto(savedPost);
    }

    private void notifySubscribers(Post post) {
        var subscriptionList = postSubscriptionRepository
                .findAllBySubscribedUserIdAndEnabled(post.getUserId(), true);
        log.debug("Found {} subscribers for user {}", subscriptionList.size(), post.getUserId());
        var messages = subscriptionList.stream()
                .map(sub -> new PostCreatedMessage(
                        sub.getSubscribedUserId(),
                        sub.getEmail(),
                        post.getTitle()
                ))
                .toList();
        messages.forEach(this::sendMessage);
    }

    @SneakyThrows
    private void sendMessage(PostCreatedMessage message) {
        amqpTemplate.convertAndSend(rabbitProperties.getExchange().getPostSubsEmailExchange(),
                "post.created",
                message);
    }
}
