package com.SocketOne.One.Listner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PresenceEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PresenceEventListener.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Map<String, String> sessionIdToUser = new ConcurrentHashMap<>();

    public PresenceEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventListener
    public void handleSessionConnect(SessionConnectedEvent event){

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionid = accessor.getSessionId();
        String username = accessor.getFirstNativeHeader("username");

        logger.info("[SessionConnectEvent] sessionId={}, username={}", sessionid, username);
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String username = accessor.getFirstNativeHeader("username");

        if (sessionId != null && username !=null && !username.isBlank()){
            sessionIdToUser.put(sessionId, username);
            logger.info("[SessionConnectedEvent] User '{}' connected with session {}", username, sessionId);
            broadCastOnlineUser();
        }else {
            logger.info("[SessionConnectedEvent] Connected with no username. sessionId={}", sessionId);
        }
    }


    @EventListener
    public void handleSessionsubscribe(SessionSubscribeEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        String username = sessionIdToUser.get(sessionId);

        logger.info("[SessionSubscribeEvent] sessionId={}, user={}, destination={}",
                sessionId, username, destination);

    }


    @EventListener
    public void handleSessinoUnsubscribe(SessionSubscribeEvent event){

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        String username = sessionIdToUser.get(sessionId);

        String userName = sessionIdToUser.remove(sessionId);

        if (userName != null){
            logger.info("[SessionDisconnectEvent] User '{}' disconnected (session={})", username, sessionId);
            broadCastOnlineUser();
        }else {
            logger.info("[SessionDisconnectEvent] Unknown session disconnected: {}", sessionId);
        }

    }

    private void broadCastOnlineUser() {
        Set<String> users = new TreeSet<>(sessionIdToUser.values());
        logger.info("Online Users : {}", users);
        simpMessagingTemplate.convertAndSend("/topic/online-users",users);
    }
}