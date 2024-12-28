package bth.ui.config;

import bth.ui.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.ClientGraphQlRequest;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.graphql.client.SyncGraphQlClientInterceptor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Configuration
@Slf4j
public class GraphQLConfig {

    @Value("${bth.ui.post-service.url}")
    private String postServiceUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestInterceptor(new HttpMdcInterceptor())
                .baseUrl(postServiceUrl)
                .build();
    }

    private static class HttpMdcInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            request.getHeaders().add("X-Correlation-ID", MDC.get("correlationId"));
            return execution.execute(request, body);
        }
    }

    @Bean
    public HttpSyncGraphQlClient graphQlClient(RestClient restClient) {
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
                throw new DataServiceException("Data service error");
            }
            return response;
        }
    }
}
