package bth.postservice.monitoring;

import bth.common.dto.filter.PostsFilterDto;
import bth.postservice.service.monitoring.MonitoringPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "bth.post-service.monitoring-enabled", havingValue = "true")
public class MonitoringAspect {

    private final MonitoringPublisherService monitoringPublisherService;

    @Pointcut("execution(* bth.postservice.resolver.PostResolver.posts(..))")
    public void postResolverPosts() {}

    @Before(value = "postResolverPosts() && args(page, filter)", argNames = "page,filter")
    public void monitorPostsQuery(int page, PostsFilterDto filter) {
        if (filter == null || filter.getQuery() == null || filter.getQuery().isBlank()) {
            return;
        }
        log.debug("monitorPostsQuery. Query: {}", filter.getQuery());
        monitoringPublisherService.publishPostsQueryEvent(filter.getQuery());
    }
}
