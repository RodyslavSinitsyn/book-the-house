package bth.imageservice.resource;

import bth.imageservice.service.S3Service;
import bth.models.contract.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ImageResource {

    private final S3Service s3Service;
    private final ImageService imageService;

    @PostMapping(value = "/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> saveImage(@RequestPart("file") FilePart file) {
            return DataBufferUtils.join(file.content())
                    .map(dataBuffer -> {
                        byte[] imageBytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(imageBytes);
                        DataBufferUtils.release(dataBuffer);
                        return imageBytes;
                    })
                    .flatMap(imageBytes -> {
                        // Assuming imageService.uploadImage is a blocking operation
                        return Mono.fromCallable(() -> imageService.uploadImage(imageBytes))
                                .subscribeOn(Schedulers.boundedElastic()) // Offload to boundedElastic for blocking I/O
                                .map(ResponseEntity::ok);
                    })
                    .onErrorResume(throwable -> {
                        log.error("Error saving image", throwable);
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving image"));
                    });
    }

    @SneakyThrows
    @GetMapping("/v1/download/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        byte[] imageBytes = s3Service.downloadImage(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @SneakyThrows
    @DeleteMapping("/v1/delete/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("imageId") String imageId) {
        s3Service.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }
}
