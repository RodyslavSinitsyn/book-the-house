package bth.common.filter;

import com.google.common.io.Closer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(10)
@Slf4j
@ConditionalOnClass(name = "jakarta.servlet.Filter")
public class LoggingMdcAppenderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try (Closer closer = Closer.create()) {
            populateMdcContext(closer, request);
            filterChain.doFilter(request, response);
        }
    }

    protected void populateMdcContext(Closer closer, HttpServletRequest request) {
        var correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                .orElse(UUID.randomUUID().toString());
        closer.register(MDC.putCloseable("correlationId", correlationId));
    }
}
