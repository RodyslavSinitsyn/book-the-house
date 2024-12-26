package bth.ui.service;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
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
    public List<PostDto> posts(int page) {
        var query =
                """
                       query($page: Int) {
                          posts(page: $page) {
                            id
                            title
                            description
                            price
                            imageUrl
                          }
                        }
                       """;
        return client.document(query)
                .variable("page", page)
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
                            description
                            price
                            imageUrl
                          }
                        }
                       """;
        return client.document(query)
                .variable("id", id)
                .retrieveSync("post")
                .toEntity(PostDto.class);
    }
}
