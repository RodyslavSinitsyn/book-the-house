package bth.notificator.config;

import bth.models.rabbitmq.RabbitExchange;
import bth.models.rabbitmq.RabbitQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue postSubsEmailQueue() {
        return new Queue(RabbitQueue.POST_SUBS_EMAIL_QUEUE.getQueueName(), true);
    }

    @Bean
    public DirectExchange postSubsEmailDirect() {
        return new DirectExchange(RabbitExchange.POST_SUBS_EMAIL_DIRECT.getExchangeName());
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
        typeMapper.setTrustedPackages("bth.models.message");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
