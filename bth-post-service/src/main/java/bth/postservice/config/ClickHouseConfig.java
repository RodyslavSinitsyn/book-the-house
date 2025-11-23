package bth.postservice.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ClickHouseConfig {

    @Bean
    public JdbcTemplate clickHouseJdbcTemplate(@Value("${spring.datasource.clickhouse.url}") String url,
                                               @Value("${spring.datasource.clickhouse.username}") String username,
                                               @Value("${spring.datasource.clickhouse.password}") String password) {
        return new JdbcTemplate(DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .type(HikariDataSource.class)
                .build());
    }
}
