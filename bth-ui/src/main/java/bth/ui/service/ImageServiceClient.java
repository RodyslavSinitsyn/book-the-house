package bth.ui.service;

import bth.models.contract.ImageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

//    public String uploadImageTEST(MultipartFile file) {
//        var headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        var body = new LinkedMultiValueMap<String, Object>();
//        body.add("file", file.getResource());
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//        return restTemplate.postForObject(imageServiceUrl + "/v1/upload", requestEntity, String.class);
//    }

    @Override
    public String uploadImage(byte[] imageBytes) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        var body = new LinkedMultiValueMap<String, Object>();
        body.add("file", new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "uploaded-file.jpg";
            }
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(imageServiceUrl + "/v1/upload", requestEntity, String.class);
    }

    @Override
    @CircuitBreaker(name = "imageService", fallbackMethod = "downloadImageFallback")
    public byte[] downloadImage(String imageId) {
        var imageData = restTemplate.getForObject(imageServiceUrl + "/v1/download/" + imageId, byte[].class);
        log.trace("Image downloaded from image-service: {}", imageId);
        return imageData;
    }

    @Override
    public void deleteImage(String imageId) {
        restTemplate.delete(imageServiceUrl + "/v1/delete/" + imageId);
    }

    private byte[] downloadImageFallback(String imageId, Throwable throwable) {
        log.warn("image-service unavailable: {}", throwable.getMessage());
        return fallbackImageData;
    }
}
