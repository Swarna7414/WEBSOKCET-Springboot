package com.SocketOne.One.Controller;

import com.SocketOne.One.DTO.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatContoller {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    private ChatMessage handleChat(ChatMessage incoming){
        if (incoming==null || incoming.getContent().trim().isEmpty()){
            return null;
        }

        incoming.setTimeStamp(Instant.now().toString());

        if (incoming.getSender()==null || incoming.getContent().trim().isEmpty()){
            return null;
        }

        incoming.setTimeStamp(Instant.now().toString());

        if (incoming.getSender() != null){
            incoming.setSender(incoming.getSender().trim());
        }

        incoming.setContent(incoming.getContent().trim());

        return incoming;
    }
}
