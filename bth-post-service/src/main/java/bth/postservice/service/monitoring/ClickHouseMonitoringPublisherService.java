package bth.postservice.service.monitoring;

import bth.postservice.service.ClickHouseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ClickHouseMonitoringPublisherService implements MonitoringPublisherService {

    private final ClickHouseService service;

    @Override
    @Async("monitoringExecutor")
    public void publishPostsQueryEvent(String rawQuery) {
        if (StringUtils.isEmpty(rawQuery)) {
            return;
        }
        var tokens = Arrays.stream(rawQuery.toLowerCase().split("\\W+"))
                .filter(s -> !s.isBlank())
                .toList();
        service.savePostsSearchQueries(tokens);
    }
}
