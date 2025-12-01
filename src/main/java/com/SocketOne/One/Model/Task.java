package com.SocketOne.One.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Task {
    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private TaskStatus taskStatus;
}
