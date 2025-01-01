package bth.imageservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AwsS3Config {

    @Value("${bth.image-service.aws.url}")
    private String awsUrl;

    @Value("${bth.image-service.aws.s3.accessKey}")
    private String accessKey;

    @Value("${bth.image-service.aws.s3.secretKey}")
    private String secretKey;

    @Value("${bth.image-service.aws.s3.region}")
    private String region;

    @Bean
    public S3Client amazonS3() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .endpointOverride(URI.create(awsUrl))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true) // Enable path-style access
                                .build()
                )
                .build();
    }
}
