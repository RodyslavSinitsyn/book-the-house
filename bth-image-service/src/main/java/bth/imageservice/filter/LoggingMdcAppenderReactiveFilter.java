package bth.imageservice.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class LoggingMdcAppenderReactiveFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.deferContextual(ctx -> {
            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Correlation-Id"))
                    .ifPresent(correlationId -> MDC.put("correlationId", correlationId));
            return chain.filter(exchange).doFinally(signalType -> MDC.clear());
        });
    }
}
