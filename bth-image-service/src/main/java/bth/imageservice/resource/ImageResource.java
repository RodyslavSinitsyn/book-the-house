package bth.imageservice.resource;

import bth.imageservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageResource {

    private final S3Service s3Service;

    @PostMapping("/v1/upload")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) {
        try {
            var imageId = s3Service.uploadImage(file.getBytes());
            return ResponseEntity.ok(imageId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving image");
        }
    }

    @SneakyThrows
    @GetMapping("/v1/download/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        byte[] imageBytes = s3Service.downloadImage(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
