package bth.postservice.service.monitoring;

public interface MonitoringPublisherService {
    void publishPostsQueryEvent(String rawQuery);
}
