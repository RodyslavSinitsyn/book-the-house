package bth.postservice.resolver;

import bth.common.contract.PostSubscriptionService;
import bth.common.dto.PostSubscriptionDto;
import bth.postservice.entity.PostSubscription;
import bth.postservice.mapper.PostSubscriptionMapper;
import bth.postservice.repo.PostSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostSubscriptionResolver implements PostSubscriptionService {

    private final PostSubscriptionRepository postSubscriptionRepository;
    private final PostSubscriptionMapper postSubscriptionMapper;

    @Override
    @QueryMapping
    public List<PostSubscriptionDto> subscriptions(@Argument("userId") String userId,
                                                   @Argument("email") String email) {
        return postSubscriptionRepository.findAllByUserIdOrEmail(userId, email).stream()
                .map(postSubscriptionMapper::toDto)
                .toList();
    }

    @Override
    @MutationMapping
    public PostSubscriptionDto subscribe(@Argument("subscribedUserId") String subscribedUserId,
                                         @Argument("email") String email,
                                         @Argument("userId") String userId) {
        var existingSubscription =
                postSubscriptionRepository.findBySubscribedUserIdAndEmailOrUserId(subscribedUserId, email, userId);
        if (existingSubscription.isPresent()) {
            // Exists, update
            existingSubscription.get().setEnabled(true);
            existingSubscription.get().setUpdatedAt(LocalDateTime.now());
            var updatedEntity = postSubscriptionRepository.save(existingSubscription.get());
            return postSubscriptionMapper.toDto(updatedEntity);
        } else {
            // Not exist, create
            var postSubscription = new PostSubscription();
            postSubscription.setEmail(email);
            postSubscription.setUserId(userId);
            postSubscription.setSubscribedUserId(subscribedUserId);
            postSubscription.setCreatedAt(LocalDateTime.now());
            postSubscription.setUpdatedAt(LocalDateTime.now());
            postSubscription.setEnabled(true);
            var savedEntity = postSubscriptionRepository.save(postSubscription);
            return postSubscriptionMapper.toDto(savedEntity);
        }
    }

    @Override
    @MutationMapping
    public PostSubscriptionDto unsubscribe(@Argument("subscribedUserId") String subscribedUserId,
                                           @Argument("email") String email,
                                           @Argument("userId") String userId) {
        var existingSubscription =
                postSubscriptionRepository.findBySubscribedUserIdAndEmailOrUserId(subscribedUserId, email, userId);
        if (existingSubscription.isPresent()) {
            existingSubscription.get().setEnabled(false);
            existingSubscription.get().setUpdatedAt(LocalDateTime.now());
            return postSubscriptionMapper.toDto(postSubscriptionRepository.save(existingSubscription.get()));
        } else {
            throw new RuntimeException("Subscription not found");
        }
    }

}
