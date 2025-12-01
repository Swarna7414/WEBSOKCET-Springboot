package com.SocketOne.One.Controller;

import com.SocketOne.One.DTO.TaskRequest;
import com.SocketOne.One.Model.Task;
import com.SocketOne.One.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest){
        Task created = taskService.createTask(taskRequest);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest request){
        Task updated = taskService.updateTask(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/id")
    public ResponseEntity<Task> getTask(@PathVariable Long id){
        Task task = taskService.gettask(id);

        if (task!=null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }


    @GetMapping
    public ResponseEntity<Collection<Task>> getalltasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }


}