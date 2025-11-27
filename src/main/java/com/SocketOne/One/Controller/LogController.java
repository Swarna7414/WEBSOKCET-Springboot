package com.SocketOne.One.Controller;


import com.SocketOne.One.Model.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LogController {

    private final SimpMessagingTemplate simpMessagingTemplate;


    public void broadcastLog(LogMessage logMessage){
        simpMessagingTemplate.convertAndSend("/topic/logs",logMessage);
    }





}
