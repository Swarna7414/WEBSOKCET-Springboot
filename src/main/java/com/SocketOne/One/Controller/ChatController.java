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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessage sendBroadCast(@Valid @Payload ChatMessage message){
        logger.info("Broadcast Message from {} and content {}",message.getSender(),message.getContent());

        message.setTimestamp(Instant.now());
        if (message.getType() == null){
            message.setType(MessageType.CHAT);
        }

        return message;
    }


    @MessageMapping("/private-message")
    public void sendPrivate(@Valid @Payload PrivateMessage message){
        message.setTimestamp(Instant.now());

        logger.info("Private Messages from {} to {}",message.getRecipient(), message.getContent());

        String destination = "/user/"+message.getRecipient()+"/queue/messages";

        simpMessagingTemplate.convertAndSend(destination, message);
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendTo("/queue/errors")
    public ErrorMessage handleValidationError(MethodArgumentNotValidException exception){

        String errors = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        logger.warn("validation error {}",errors);

        return new ErrorMessage(errors);
    }

    @MessageExceptionHandler(Exception.class)
    @SendTo("/queue/errors")
    public ErrorMessage handleOthers(Exception exception){
        logger.info("Unexpected error");
        return new ErrorMessage("Server Error :"+exception.getClass().getSimpleName()+"-"+exception.getMessage());
    }

}