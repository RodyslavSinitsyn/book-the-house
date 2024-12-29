package bth.ui.service;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
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
public class PostServiceClient implements PostService {

    private final HttpSyncGraphQlClient client;
    private final RedisWrapper redisWrapper;

    @Override
    @Retry(name = "postService", fallbackMethod = "postsFallback")
    public List<PostDto> posts(int page, PostsFilterDto filter) {
        var query =
                """
                        query($page: Int, $filter: PostFilterInput) {
                           posts(page: $page, filter: $filter) {
                            id
                            title
                            status,
                            imageUrl,
                            details {
                              description
                              availableFrom
                              availableTo
                              price
                            }
                            location {
                              country
                              city
                              street
                              houseNumber
                            }
                          }
                         }
                        """;
        return client.document(query)
                .variable("page", page)
                .variable("filter", filter)
                .retrieveSync("posts")
                .toEntityList(PostDto.class);
    }

    @Override
    @Retry(name = "postService")
    public PostDto post(String id) {
        var query =
                """
                        query($id: String) {
                           post(id: $id) {
                            id
                            title
                            status,
                            imageUrl,
                            details {
                              description
                              availableFrom
                              availableTo
                              price
                            }
                            location {
                              country
                              city
                              street
                              houseNumber
                            }
                          }
                        }
                        """;
        return client.document(query)
                .variable("id", id)
                .retrieveSync("post")
                .toEntity(PostDto.class);
    }

    @Override
    @Retry(name = "postService")
    public PostDto createPost(String imageUrl) {
        try {
            var mutation = """
                    mutation($imageUrl: String) {
                      createPost(imageUrl: $imageUrl) {
                        id
                        title
                        status
                        imageUrl
                        details {
                          description
                          availableFrom
                          availableTo
                          price
                        }
                        location {
                          country
                          city
                          street
                          houseNumber
                        }
                      }
                    }
                    """;
            return client.document(mutation)
                    .variable("imageUrl", imageUrl)
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
}
