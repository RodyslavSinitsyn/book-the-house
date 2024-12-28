package bth.ui.controller;

import bth.models.contract.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
public class ImageController {

    @Value("${bth.ui.images.cache-control-min}")
    private int minutes;

    private final ImageService imageService;

    @GetMapping("/images/{imageId}")
    @SneakyThrows
    @ResponseBody
    public ResponseEntity<byte[]> image(@PathVariable("imageId") String imageId) {
        var imageBytes = imageService.downloadImage(imageId);
        String eTag = DigestUtils.md5DigestAsHex(imageBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(minutes)))
                .eTag(eTag)
                .body(imageBytes);
    }
}
