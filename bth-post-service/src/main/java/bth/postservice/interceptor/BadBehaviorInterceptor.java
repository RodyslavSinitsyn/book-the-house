package bth.postservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class BadBehaviorInterceptor implements HandlerInterceptor {

    private final Random random = new SecureRandom();

    private int unavailabilityPercentage = 0;
    private int mockDelaySeconds = 5;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int chance = random.nextInt(100);
        if (chance < unavailabilityPercentage) {
            log.warn("Unavailable, sorry...");
            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable (Mocked)");
            return false;
        }
        if (chance < unavailabilityPercentage + 25) {
            log.warn("Sleep, sorry...");
            TimeUnit.SECONDS.sleep(mockDelaySeconds);
        }
        return true;
    }
}
