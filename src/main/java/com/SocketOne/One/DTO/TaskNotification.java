package com.SocketOne.One.DTO;

import com.SocketOne.One.Model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskNotification {
    private Long taskId;
    private Long projectId;
    private String title;
    private TaskStatus status;
    private String action;
}