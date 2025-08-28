package bth.postservice.config;

import bth.postservice.exception.ModelMapperConversionException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<UUID, String> uuidToStringConverter = context ->
                Optional.ofNullable(context)
                        .map(MappingContext::getSource)
                        .map(UUID::toString)
                        .orElseThrow(() -> new ModelMapperConversionException("UUID to string"));

        Converter<String, UUID> stringToUuidConverter = context ->
                Optional.ofNullable(context)
                        .map(MappingContext::getSource)
                        .map(UUID::fromString)
                        .orElseThrow(() -> new ModelMapperConversionException("String to UUID"));

        modelMapper.addConverter(uuidToStringConverter);
        modelMapper.addConverter(stringToUuidConverter);

        return modelMapper;
    }
}
