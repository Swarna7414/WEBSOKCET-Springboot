package com.SocketOne.One.Config;


import com.SocketOne.One.Model.ChatMessage;
import com.SocketOne.One.Model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListner {

    private final SimpMessageSendingOperations messageTemplete;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = headerAccessor.getSessionAttributes().get("username").toString();
        if (userName !=null){
            log.info("user Disconnected {}",userName);
            var chatMessage = ChatMessage.builder().messaageType(MessageType.LEAVER).sender(userName).build();

            messageTemplete.convertAndSend("/topic/public",chatMessage);
        }
    }
}
