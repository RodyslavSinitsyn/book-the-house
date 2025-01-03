package bth.postservice.repo.elastic;

import bth.postservice.entity.PostDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, UUID> {
    List<PostDocument> findAllByTitleOrDescriptionContainingIgnoreCase(String title, String description);


    @SneakyThrows
    default List<PostDocument> findMultimatch(String queryVal, ElasticsearchClient client) {
        // Create a MultiMatch query to search across title and description fields
        var multiMatchQuery = new MultiMatchQuery.Builder()
                .query(queryVal)
                .fields("title", "description")
                .fuzziness("2")
                .build();
        var searchResponse = client.search(s -> s.index("posts").query(q -> q.multiMatch(multiMatchQuery)), PostDocument.class);
        return searchResponse.hits().hits().stream().map(Hit::source).toList();
    }

    @SneakyThrows
    default List<PostDocument> findFuzzy(String queryVal, ElasticsearchClient client) {
        var query = Query.of(q -> q.bool(b -> b
                .should(s -> s.fuzzy(fuzzyQuery("title", queryVal)))
                .should(s -> s.fuzzy(fuzzyQuery("description", queryVal)))
        ));
        var searchResponse = client.search(s -> s.index("posts").query(query), PostDocument.class);
        return searchResponse.hits().hits().stream().map(Hit::source).toList();
    }

    private FuzzyQuery fuzzyQuery(String field, String queryVal) {
        return new FuzzyQuery.Builder()
                .field(field)
                .value(queryVal)
                .fuzziness("2")
                .transpositions(true)
                .build();
    }
}
