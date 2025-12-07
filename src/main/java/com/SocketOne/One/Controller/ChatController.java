package com.SocketOne.One.Controller;

import com.SocketOne.One.DTO.ChatMessage;
import com.SocketOne.One.DTO.ErrorMessage;
import com.SocketOne.One.DTO.MessageType;
import com.SocketOne.One.DTO.PrivateMessage;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.stream.Collectors;


public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessage sendBroadCast(@Valid @Payload ChatMessage message){
        logger.info("BroadCast Message from {} to {}", message.getSender() , message.getContent());
        message.setTimestamp(Instant.now());

        if (message.getType() == null){
            message.setType(MessageType.CHAT);
        }

        return message;
    }


    @MessageMapping("private-message")
    public void SendPrivatemessage(PrivateMessage message){
        message.setTimestamp(Instant.now());

        String destination = "/user/"+message.getRecipient()+"/queue/messages";

        logger.info("Message was sent to {} form {}",message.getRecipient(), message.getSender());

        messagingTemplate.convertAndSend(destination, message);

    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendTo("/queue/errors")
    public ErrorMessage handleValidationError(MethodArgumentNotValidException exception){

        String errors = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        logger.info("Validation Error : {}", errors);

        return new ErrorMessage(errors);
    }


    @MessageExceptionHandler(Exception.class)
    @SendTo("/queue/errors")
    public ErrorMessage handleErrorMessage(Exception exception){
        logger.info("Unexpected Error", exception);
        return new ErrorMessage(
                "Server Error " + exception.getClass().getSimpleName() + " - " + exception.getMessage()
        );
    }


}