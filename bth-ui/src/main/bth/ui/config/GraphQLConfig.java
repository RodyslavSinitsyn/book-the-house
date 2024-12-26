package bth.ui.config;

import bth.ui.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.ClientGraphQlRequest;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.graphql.client.SyncGraphQlClientInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
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
        return HttpSyncGraphQlClient.builder(restClient)
                .interceptor(new ErrorHandlerInterceptor())
                .build();
    }

    private class ErrorHandlerInterceptor implements SyncGraphQlClientInterceptor {
        @Override
        public ClientGraphQlResponse intercept(ClientGraphQlRequest request, Chain chain) {
            var response = chain.next(request);
            if (!CollectionUtils.isEmpty(response.getErrors())) {
                log.error("Error occurred while executing GraphQL query: {}", response.getErrors());
                throw new DataServiceException("Data service error");
            }
            return response;
        }
    }
}
