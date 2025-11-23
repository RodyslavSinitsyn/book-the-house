package bth.ui.config;

import bth.ui.converter.OAuth2ToCurrentAppUserConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ConverterConfig {

    @Bean("oauth2ToCurrentAppUserConverters")
    public Map<String, OAuth2ToCurrentAppUserConverter> oauth2ToCurrentAppUserConverters(
            List<OAuth2ToCurrentAppUserConverter> converters) {
        return converters.stream()
                .collect(Collectors.toMap(
                        OAuth2ToCurrentAppUserConverter::externalSystemName, Function.identity()));
    }
}
