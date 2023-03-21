package com.example.amqp_test.rabbit;

import com.example.amqp_test.AnotherMessageDTO;
import com.example.amqp_test.MessageDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "in")
public class RabbitQueueListener {

    @RabbitHandler
    @SendTo("out")
    public MessageDTO handle(MessageDTO messageDTO){
        System.out.printf("In queue listener: %s%n", messageDTO.toString());
        return messageDTO;
    }

    @RabbitHandler
    @SendTo("out")
    public AnotherMessageDTO handleAnotherMessage(AnotherMessageDTO anotherMessageDTO){
        System.out.printf("In queue listener: %s%n", anotherMessageDTO.toString());
        System.out.println("This is another message handler");
        return anotherMessageDTO;
    }
}
