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
public class TaskRequest {
    private String title;
    private String description;
    private Long projectId;
    private TaskStatus taskStatus;
}