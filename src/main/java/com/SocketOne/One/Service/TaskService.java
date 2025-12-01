package com.SocketOne.One.Service;

import com.SocketOne.One.DTO.TaskNotification;
import com.SocketOne.One.DTO.TaskRequest;
import com.SocketOne.One.Model.Task;
import com.SocketOne.One.Model.TaskStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    private final SimpMessagingTemplate messagingTemplate;

    public TaskService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public Task createTask(TaskRequest request){
        Long id = idGenerator.getAndIncrement();
        TaskStatus status = request.getTaskStatus() != null ? request.getTaskStatus() : TaskStatus.TODO;


        Task task = new Task(id, request.getTitle(), request.getDescription(), request.getProjectId(), status);

        tasks.put(id,task);

        sendNotification(task,"Created");

        return task;
    }


    public Task updateTask(Long id, TaskRequest request){
        Task existing = tasks.get(id);

        if (existing == null){
            throw new IllegalArgumentException("Task Not Found"+id);
        }

        if (request.getTitle() != null){
            existing.setTitle(request.getTitle());
        }
        if (request.getDescription() != null){
            existing.setDescription(request.getDescription());
        }
        if (request.getProjectId() != null){
            existing.setProjectId(request.getProjectId());
        }
        if (request.getTaskStatus() != null){
            existing.setTaskStatus(request.getTaskStatus());
        }

        sendNotification(existing, "UPDATED");

        return existing;
    }


    public Task gettask(Long id){
        return tasks.get(id);
    }


    public Collection<Task> getAllTasks(){
        return tasks.values();
    }



    private void sendNotification(Task task,String action) {
        TaskNotification notification = new TaskNotification(task.getId(), task.getProjectId(), task.getTitle(),
                task.getTaskStatus(), action);

        messagingTemplate.convertAndSend("/topic/tasks",notification);

        if (task.getProjectId() != null){
            String destination = "/topic/tasks." + task.getProjectId();
            messagingTemplate.convertAndSend(destination, notification);
        }
    }


}