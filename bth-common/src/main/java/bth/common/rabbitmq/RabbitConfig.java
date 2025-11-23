package bth.common.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
@Slf4j
@Profile({"rabbit", "rabbit-docker", "rabbit-prod"})
public class RabbitConfig {

    @Autowired
    private RabbitProperties properties;

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               Jackson2JsonMessageConverter
                                                                                       jackson2JsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter);
        factory.setAfterReceivePostProcessors(message -> {
            log.trace("Received message, id: {} to correlationId: {}",
                    message.getMessageProperties().getMessageId(),
                    message.getMessageProperties().getCorrelationId());
            return message;
        });
        return factory;
    }

    @Bean
    public Queue postSubsEmailQueue() {
        return new Queue(properties.getQueue().getPostSubsEmailQueue(), true);
    }

    @Bean
    public DirectExchange postSubsEmailDirect() {
        return new DirectExchange(properties.getExchange().getPostSubsEmailExchange());
    }

    @Bean
    public Binding binding(Queue postSubEmailQueue,
                           DirectExchange postSubsEmailDirect) {
        return BindingBuilder.bind(postSubEmailQueue)
                .to(postSubsEmailDirect)
                .with("post.created");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("bth.models.rabbitmq.message");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public MessagePostProcessor mdcMessagePostProcessor() {
        return message -> {
            Optional.ofNullable(MDC.get("correlationId"))
                    .ifPresent(correlationId -> {
                                log.debug("Appending correlationId: {}, to message", correlationId);
                                message.getMessageProperties().setCorrelationId(correlationId);
                            }
                    );
            return message;
        };
    }
}
