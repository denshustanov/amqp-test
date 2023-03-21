package com.example.amqp_test.rabbit;

import com.example.amqp_test.MessageDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {
    private final RabbitTemplate rabbitTemplate;

    public TestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/test-send")
    void send(@RequestParam(name = "exchange") String exchange,
              @RequestParam(name = "routingKey") String routingKey,
              @RequestParam(name = "message") String payload){
        MessageProperties properties = new MessageProperties();
        properties.setMessageId(UUID.randomUUID().toString());
        Message message = new Message(
                payload.getBytes(),
                properties
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @GetMapping("/test-read")
    String read(@RequestParam(name = "queue") String queue){
        Message message = rabbitTemplate.receive(queue);
        return new String(message.getBody());
    }

    @GetMapping("/send-receive")
    String sendRec(@RequestParam String payload){
        Message request = MessageBuilder.withBody(payload.getBytes())
                .setReplyTo("out")
                .build();
        Message response = rabbitTemplate.sendAndReceive("in", request);

        return new String(response.getBody());
    }

    @GetMapping("/send-with-body")
    MessageDTO sendObject(@RequestBody MessageDTO messageDTO){
        System.out.printf("In controller: %s%n", messageDTO.toString());
        MessageDTO received = rabbitTemplate.convertSendAndReceiveAsType("in", messageDTO, ParameterizedTypeReference.forType(MessageDTO.class));
        System.out.printf("In controller: %s%n", received.toString());
        return received;
    }
}
