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
        var builder = new RestTemplateBuilder();
        builder.readTimeout(Duration.ofSeconds(10));
        builder.interceptors(mdcAppenderHttpInterceptor);
        return builder.build();
    }
}
