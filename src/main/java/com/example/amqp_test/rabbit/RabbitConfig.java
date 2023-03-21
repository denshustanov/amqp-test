package com.example.amqp_test.rabbit;

import com.example.amqp_test.AnotherMessageDTO;
import com.example.amqp_test.MessageDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Configuration
public class RabbitConfig {
    public static final String MESSAGE_TYPE_MESSAGE = "message";
    public static final String MESSAGE_TYPE_ANOTHER = "another_message";

    @Bean
    ConnectionFactory connectionFactory(){
        return new CachingConnectionFactory();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        factory.setAfterReceivePostProcessors(message -> {
            String typeId = null;

            var type = message.getMessageProperties().getHeaders().get("type");

            if(type.equals(MESSAGE_TYPE_MESSAGE)){
                typeId = MessageDTO.class.getName();
            } else if(type.equals(MESSAGE_TYPE_ANOTHER)){
                typeId = AnotherMessageDTO.class.getName();
            }

            Optional.ofNullable(typeId).ifPresent(t -> message.getMessageProperties().setHeader("__TypeId__", t));

            return message;
        });

        return factory;
    }

}
