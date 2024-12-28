package bth.ui.service;

import bth.models.contract.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ImageServiceClient implements ImageService {

    @Value("${bth.ui.image-service.url}")
    private String imageServiceUrl;

    private final RestTemplate restTemplate;

    public ImageServiceClient(@Qualifier("imageServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String uploadImage(byte[] imageBytes) {
        throw new UnsupportedOperationException("Not supported uploading images from UI");
    }

    @Override
    public byte[] downloadImage(String imageId) {
        var imageData = restTemplate.getForObject(imageServiceUrl + "/v1/download/1",
                byte[].class);
        log.trace("Image downloaded from image-service: {}", imageId);
        return imageData;
    }
}
