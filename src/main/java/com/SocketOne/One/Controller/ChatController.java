package com.SocketOne.One.Controller;

import com.SocketOne.One.DTO.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    private ChatMessage handleChat(ChatMessage incoming){
        if (incoming == null) return null;
        return incoming;
    }
}