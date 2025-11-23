package bth.postservice.repo.elastic;

import bth.postservice.entity.PostDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, UUID> {

    @SneakyThrows
    default List<PostDocument> searchPosts(String queryVal, int size, ElasticsearchClient client) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                match -> match.query(queryVal)
                        .fields("title^2", "description")
                        .type(TextQueryType.MostFields)
                        .operator(Operator.Or)
                        .fuzziness("AUTO")
                        .minimumShouldMatch("50%")
                        .analyzer("english"));

        Query query = Query.of(q -> q.multiMatch(multiMatchQuery));

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("posts")
                .query(query)
                .size(size));

        var response = client.search(searchRequest, PostDocument.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }
}
