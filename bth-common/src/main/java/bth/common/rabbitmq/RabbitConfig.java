package bth.common.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
@Profile("rabbit")
public class RabbitConfig {

    @Autowired
    private RabbitProperties properties;

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
}