package bth.ui.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Order(20)
public class RequestTimeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Proceed with the request
        filterChain.doFilter(request, response);

        stopWatch.stop();
        if (isStaticContent(request.getRequestURI())) {
            log.trace("Static request completed. {}, {} ms",
                    request.getRequestURI(), stopWatch.getTotalTime(TimeUnit.MILLISECONDS));
        } else {
            log.debug("HTTP request completed. {}, {} ms",
                    request.getRequestURI(), stopWatch.getTotalTime(TimeUnit.MILLISECONDS));
        }
    }

    private boolean isStaticContent(String uri) {
        return uri.matches(".*\\.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot|map)$");
    }
}
