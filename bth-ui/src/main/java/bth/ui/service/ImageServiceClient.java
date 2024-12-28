package bth.ui.service;

import bth.models.contract.ImageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class ImageServiceClient implements ImageService {

    private byte[] fallbackImageData = new byte[0];

    @Value("${bth.ui.image-service.url}")
    private String imageServiceUrl;

    private final RestTemplate restTemplate;

    public ImageServiceClient(@Qualifier("imageServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        try (InputStream resourceAsStream = getClass().getResourceAsStream("/static/img/house.jpeg")) {
            fallbackImageData = resourceAsStream.readAllBytes();
        } catch (IOException e) {
            log.warn("Failed to load fallback image data", e);
        }
    }

    @Override
    public String uploadImage(byte[] imageBytes) {
        throw new UnsupportedOperationException("Not supported uploading images from UI");
    }

    @Override
    @CircuitBreaker(name = "imageService", fallbackMethod = "downloadImageFallback")
    public byte[] downloadImage(String imageId) {
        var imageData = restTemplate.getForObject(imageServiceUrl + "/v1/download/1",
                byte[].class);
        log.trace("Image downloaded from image-service: {}", imageId);
        return imageData;
    }

    private byte[] downloadImageFallback(String imageId, Throwable throwable) {
        log.warn("image-service unavailable: {}", throwable.getMessage());
        return fallbackImageData;
    }
}
