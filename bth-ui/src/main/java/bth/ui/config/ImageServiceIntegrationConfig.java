package bth.ui.config;

import bth.ui.filter.MdcAppenderHttpInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ImageServiceIntegrationConfig {

    @Bean
    @Qualifier("imageServiceRestTemplate")
    public RestTemplate restTemplate(MdcAppenderHttpInterceptor mdcAppenderHttpInterceptor) {
        return new RestTemplateBuilder()
                .readTimeout(Duration.ofSeconds(10))
                .interceptors(mdcAppenderHttpInterceptor)
                .build();
    }
}
