package bth.ui.config;

import bth.ui.exception.PostServiceException;
import bth.ui.filter.MdcAppenderHttpInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class PostServiceIntegrationConfig {

    @Value("${bth.ui.post-service.url}")
    private String postServiceUrl;

    @Bean
    @Qualifier("postServiceRestClient")
    public RestClient restClient(MdcAppenderHttpInterceptor mdcAppenderHttpInterceptor) {
        return RestClient.builder()
                .requestInterceptor(mdcAppenderHttpInterceptor)
                .baseUrl(postServiceUrl)
                .build();
    }

    @Bean
    public HttpSyncGraphQlClient graphQlClient(@Qualifier("postServiceRestClient") RestClient restClient) {
        return HttpSyncGraphQlClient.builder(restClient)
                .interceptor(new ErrorHandlerInterceptor())
                .build();
    }

    private static class ErrorHandlerInterceptor implements SyncGraphQlClientInterceptor {
        @Override
        public ClientGraphQlResponse intercept(ClientGraphQlRequest request, Chain chain) {
            var response = chain.next(request);
            if (!CollectionUtils.isEmpty(response.getErrors())) {
                log.error("Error occurred while executing GraphQL query: {}", response.getErrors());
                throw new PostServiceException("Post service error");
            }
            return response;
        }
    }
}
