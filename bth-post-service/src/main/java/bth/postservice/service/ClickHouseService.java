package bth.postservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClickHouseService {

    private final JdbcTemplate clickHouseJdbcTemplate;

    public void savePostsSearchQueries(List<String> words) {
        String sql = "INSERT INTO posts_search_queries (word) VALUES (?)";
        List<Object[]> batchArgs = words.stream()
                .map(word -> new Object[]{word})
                .toList();
        clickHouseJdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
