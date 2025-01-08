package bth.ui.service;

import bth.common.contract.PostService;
import bth.common.contract.PostSubscriptionService;
import bth.common.contract.UserService;
import bth.common.dto.PostDto;
import bth.common.dto.PostSubscriptionDto;
import bth.common.dto.filter.PostsFilterDto;
import bth.ui.exception.PostServiceException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class PostServiceClient implements PostService, PostSubscriptionService, UserService {

    private final HttpSyncGraphQlClient client;
    private final RedisWrapper redisWrapper;

    @Override
    @Retry(name = "postService", fallbackMethod = "postsFallback")
    public List<PostDto> posts(int page, PostsFilterDto filter) {
        var query =
                """
                        query($page: Int, $filter: PostFilterInput) {
                           posts(page: $page, filter: $filter) {
                           %s
                          }
                         }
                        """.formatted(postFields());
        return client.document(query)
                .variable("page", page)
                .variable("filter", filter)
                .retrieveSync("posts")
                .toEntityList(PostDto.class);
    }

    @Override
    @Retry(name = "postService")
    public List<PostDto> nearestPosts(double longitude, double latitude) {
        var query =
                """
                        query($longitude: Float, $latitude: Float) {
                           nearestPosts(longitude: $longitude, latitude: $latitude) {
                           %s
                          }
                         }
                        """.formatted(postFields());
        return client.document(query)
                .variable("longitude", longitude)
                .variable("latitude", latitude)
                .retrieveSync("nearestPosts")
                .toEntityList(PostDto.class);
    }

    @Override
    @Retry(name = "postService")
    public PostDto post(String id) {
        var query =
                """
                        query($id: String) {
                           post(id: $id) {
                            %s
                           }
                        }
                        """.formatted(postFields());
        return client.document(query)
                .variable("id", id)
                .retrieveSync("post")
                .toEntity(PostDto.class);
    }

    @Override
    @Retry(name = "postService")
    public PostDto createPost(String imageUrl, String userId) {
        try {
            var mutation = """
                    mutation($imageUrl: String, $userId: String) {
                      createPost(imageUrl: $imageUrl, userId: $userId) {
                      %s
                      }
                    }
                    """.formatted(postFields());
            return client.document(mutation)
                    .variable("imageUrl", imageUrl)
                    .variable("userId", userId)
                    .retrieveSync("createPost")
                    .toEntity(PostDto.class);
        } catch (Exception e) {
            throw new PostServiceException(e.getMessage(), e);
        }
    }

    private List<PostDto> postsFallback(int page, PostsFilterDto filter, Throwable e) {
        log.warn("post-service unavailable: {}. ex class: {}", e.getMessage(), e.getClass().getName());
        return redisWrapper.globalGetList("posts_" + page, PostDto.class);
    }

    private String postFields() {
        return """
                id
                title
                status
                imageUrl
                username
                friendlyName
                details {
                  description
                  availableFrom
                  availableTo
                  price
                }
                location {
                  country
                  state
                  city
                  street
                  houseNumber
                }
                """;
    }

    @Override
    public List<PostSubscriptionDto> subscriptions(String userId, String email) {
        var query =
                """
                        query($userId: String, $email: String) {
                           subscriptions(userId: $userId, email: $email) {
                           id
                           userId
                           email
                           subscribedUserId
                           createdAt
                           updatedAt
                           enabled
                          }
                         }
                        """;
        return client.document(query)
                .variable("userId", userId)
                .variable("email", email)
                .retrieveSync("subscriptions")
                .toEntityList(PostSubscriptionDto.class);
    }

    @Override
    public PostSubscriptionDto subscribe(String subscribedUserId, String email, String userId) {
        var mutation =
                """
                        mutation($subscribedUserId: String, $email: String, $userId: String) {
                           subscribe(subscribedUserId: $subscribedUserId, email: $email, userId: $userId) {
                           id
                           userId
                           email
                           subscribedUserId
                           createdAt
                           updatedAt
                           enabled
                          }
                         }
                        """;
        return client.document(mutation)
                .variable("subscribedUserId", subscribedUserId)
                .variable("email", email)
                .variable("userId", userId)
                .retrieveSync("subscribe")
                .toEntity(PostSubscriptionDto.class);
    }

    @Override
    public PostSubscriptionDto unsubscribe(String subscribedUserId, String email, String userId) {
        var mutation =
                """
                        mutation($subscribedUserId: String, $email: String, $userId: String) {
                           unsubscribe(subscribedUserId: $subscribedUserId, email: $email, userId: $userId) {
                           id
                           userId
                           email
                           subscribedUserId
                           createdAt
                           updatedAt
                           enabled
                          }
                         }
                        """;
        return client.document(mutation)
                .variable("subscribedUserId", subscribedUserId)
                .variable("email", email)
                .variable("userId", userId)
                .retrieveSync("unsubscribe")
                .toEntity(PostSubscriptionDto.class);
    }

    @Override
    public String registerIfNotExist(String username, String friendlyUsername, String email) {
        var mutation =
                """
                mutation($username: String!, $friendlyUsername: String, $email: String) {
                  registerIfNotExist(username: $username, friendlyUsername: $friendlyUsername, email: $email)
                }
                """;
        return client.document(mutation)
                .variable("username", username)
                .variable("friendlyUsername", friendlyUsername)
                .variable("email", email)
                .retrieveSync("registerIfNotExist")
                .toEntity(String.class);
    }
}
