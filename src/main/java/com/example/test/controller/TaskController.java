package com.example.test.controller;

import com.example.test.dto.Performer;
import com.example.test.model.Task;
import com.example.test.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    @PostMapping("/new")
    public ResponseEntity<?> createNewTask(@Valid @RequestBody Task task, Principal principal) {
        return service.saveTask(task, principal.getName());
    }

    @GetMapping
    public ResponseEntity<List<Task>> showAllTasks() {
        return new ResponseEntity<>(service.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> showTask(@PathVariable Long id) {
        return new ResponseEntity<>(service.findTask(id), HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<List<Task>> showAuthorTask(@PathVariable Long id) {
        return new ResponseEntity<>(service.getTasksByAuthor(id), HttpStatus.OK);
    }

    @GetMapping("/performer/{id}")
    public ResponseEntity<List<Task>> showPerformerTask(@PathVariable Long id) {
        return new ResponseEntity<>(service.getTasksByPerformer(id), HttpStatus.OK);
    }

    @PutMapping("/author")
    public ResponseEntity<?> editTask(@Valid @RequestBody Task task, Principal principal) {
        return service.editTask(task, principal.getName());
    }

    @PutMapping("/performer")
    public ResponseEntity<?> editTaskStatus(@Valid @RequestBody Task task, Principal principal) {
        return service.editTaskStatus(task, principal.getName());
    }

    @PutMapping("/{taskId}/author")
    public ResponseEntity<?> appointAnExecutor(@Valid @RequestBody Performer performer,
                                               @PathVariable Long taskId, Principal principal) {
        return service.appointAnExecutor(performer, taskId, principal.getName());
    }

    @PutMapping("/{taskId}/performer")
    public ResponseEntity<?> takeTask(@PathVariable Long taskId, Principal principal) {
        return service.takeTask(taskId, principal.getName());
    }

    @DeleteMapping("/{id}/author")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return service.deleteTask(id);
    }
}
