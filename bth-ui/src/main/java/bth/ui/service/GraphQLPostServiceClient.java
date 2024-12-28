package bth.ui.service;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
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
public class GraphQLPostServiceClient implements PostService {

    private final HttpSyncGraphQlClient client;

    @Override
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
    public PostDto createPost() {
        var mutation = """
                mutation {
                  createPost {
                    id
                  }
                }
                """;
        return client.document(mutation)
                .retrieveSync("post")
                .toEntity(PostDto.class);
    }
}
