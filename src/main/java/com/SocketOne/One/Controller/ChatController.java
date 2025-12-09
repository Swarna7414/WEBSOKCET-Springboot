package com.SocketOne.One.Controller;

import com.SocketOne.One.DTO.ChatMessage;
import com.SocketOne.One.DTO.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;


    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/chat.public")
    @SendTo("/topic/public")
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage){
        validateMessage(chatMessage);
        logger.info("Public message from {}: {}",chatMessage.getSender(),chatMessage.getContent());
        return chatMessage;
    }


    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage){
        validateMessage(chatMessage);
        if (chatMessage.getReceiver() == null  || chatMessage.getReceiver().isBlank()){
            throw new IllegalArgumentException("Sender Must not be empty");
        }

        logger.info("Message to private Person from {} to {} and message {}",chatMessage.getSender(), chatMessage.getReceiver(), chatMessage.getContent());

        simpMessagingTemplate.convertAndSend(
                "/queue/private." + chatMessage.getReceiver(),
                chatMessage
        );
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ErrorMessage handleErrorMessage(Exception exception){
        logger.error("WebSocket Error",exception);
        return new ErrorMessage("Error"+exception.getMessage());
    }


    private void validateMessage(ChatMessage message) {
        if (message.getSender() == null || message.getSender().isBlank()){
            throw new IllegalArgumentException("Sender cannot be empty");
        }
        if (message.getContent() == null || message.getContent().isBlank()){
            throw new IllegalArgumentException("Content can't be empty");
        }

    }



}