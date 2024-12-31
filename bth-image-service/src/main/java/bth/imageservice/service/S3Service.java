package bth.imageservice.service;

import bth.common.contract.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service implements ImageService {
    private final S3Client s3Client;

    @Value("${bth.image-service.aws.s3.bucket}")
    private String bucketName;

    public String uploadImage(byte[] imageBytes) {
        var imageId = UUID.randomUUID().toString();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(imageId)
                        .contentType(MediaType.IMAGE_JPEG_VALUE)
                        .build(),
                RequestBody.fromBytes(imageBytes)
        );
        log.info("Upload image {} to bucket {}", imageId, bucketName);
        return imageId;
    }

    public byte[] downloadImage(String imageId) {
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(imageId)
                        .build()
        );
        log.info("Download image {} from bucket {}", imageId, bucketName);
        return objectBytes.asByteArray();
    }

    @Override
    public void deleteImage(String imageId) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(imageId)
                .build());
    }
}
