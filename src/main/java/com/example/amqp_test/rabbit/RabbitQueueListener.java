package com.example.amqp_test.rabbit;

import com.example.amqp_test.MessageDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class RabbitQueueListener {
    private final RabbitTemplate rabbitTemplate;

    public RabbitQueueListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "in")
    @SendTo("out")
    public MessageDTO handle(MessageDTO messageDTO){
        System.out.printf("In queue listener: %s%n", messageDTO.toString());
        return messageDTO;
    }
}
