package bth.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

@Configuration
public class GraphQLConfig {

    @Value("${bth.ui.data-service.url}")
    private String dataServiceUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(dataServiceUrl)
                .build();
    }

    @Bean
    public HttpSyncGraphQlClient graphQlClient(RestClient restClient) {
        return HttpSyncGraphQlClient.builder(restClient).build();
    }
}
