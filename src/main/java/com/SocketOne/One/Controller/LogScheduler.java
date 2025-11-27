package com.SocketOne.One.Controller;

import com.SocketOne.One.Model.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class LogScheduler {

    private final LogController logController;

    @Scheduled(fixedRate = 2000)
    public void generateLog() {
        System.out.println("Scheduler running..."+LocalTime.now());
        LogMessage message = new LogMessage(LocalTime.now().toString(), "Generated Log Message" + LocalTime.now());
        logController.broadcastLog(message);
    }

}