package com.example.test.service;

import com.example.test.dto.Performer;
import com.example.test.exception.AuthException;
import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> saveTask(Task task, String username) {
        if (taskRepository.findByTitle(task.getTitle()) != null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "A task with the specified title already exists\n"), HttpStatus.BAD_REQUEST);
        }
        task.setAuthor(userRepository.findByUsername(username));
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public Task findTask(Long id) {
        return taskRepository.findById(id).get();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByAuthor(Long id) {
        return taskRepository.findAllByAuthor(userRepository.findById(id).get());
    }

    public List<Task> getTasksByPerformer(Long id) {
        return taskRepository.findAllByPerformer(userRepository.findById(id).get());
    }

    public ResponseEntity<?> editTask(Task editedTask, String username) {
        Task task;
        if ((task = taskRepository.findByTitle(editedTask.getTitle())) == null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "there is no task with this title\n"), HttpStatus.BAD_REQUEST);
        } else if (!task.getAuthor().getUsername().equals(username)) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "you cannot edit this task\n"), HttpStatus.BAD_REQUEST);
        }
        editedTask.setId(task.getId());
        editedTask.setAuthor(task.getAuthor());
        editedTask.setPerformer(task.getPerformer());
        return ResponseEntity.ok(taskRepository.save(editedTask));
    }

    public ResponseEntity<?> editTaskStatus(Task editedTask, String username) {
        Task task = taskRepository.findByTitle(editedTask.getTitle());
        if (!task.getPerformer().getUsername().equals(username)
                && !task.getAuthor().getUsername().equals(username)) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "you cannot edit status this task\n"), HttpStatus.BAD_REQUEST);
        }
        task.setStatus(editedTask.getStatus());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public ResponseEntity<?> appointAnExecutor(Performer performer, Long taskId, String username) {
        User user;
        Task task = taskRepository.findById(taskId).get();
        if (!task.getAuthor().getUsername().equals(username)){
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "you cannot edit performer this task\n"), HttpStatus.BAD_REQUEST);
        } else if ((user = userRepository.findByUsername(performer.getUsername())) == null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "that performer doesn't exist\n"), HttpStatus.BAD_REQUEST);
        }
        task.setPerformer(user);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public ResponseEntity<?> takeTask(Long taskId, String username) {
        Task task = taskRepository.findById(taskId).get();
        User user = userRepository.findByUsername(username);
        if (task.getPerformer() != null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "this task is busy\n"), HttpStatus.BAD_REQUEST);
        }
        task.setPerformer(user);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public ResponseEntity<HttpStatus> deleteTask(Long id) {
        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
