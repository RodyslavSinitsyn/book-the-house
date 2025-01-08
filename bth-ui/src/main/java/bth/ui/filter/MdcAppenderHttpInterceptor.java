package bth.ui.filter;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class MdcAppenderHttpInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution)
            throws IOException {
        Optional.ofNullable(MDC.get("correlationId"))
                .ifPresent(correlationId ->
                        request.getHeaders().add("X-Correlation-ID", correlationId));
        return execution.execute(request, body);
    }
}
