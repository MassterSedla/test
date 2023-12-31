package com.example.test.service;

import com.example.test.dto.Performer;
import com.example.test.model.Role;
import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskService taskService;
    private ResponseEntity<?> response;
    private Task task;
    private User user;
    private Long taskId;
    private String title;
    private String username;

    @BeforeEach
    void setUp() {
        username = "user";
        taskId = 1L;
        title = "title";
        user = new User("user@gmail.com", username, "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setId(1L);
        task = new Task();
        task.setId(taskId);
        task.setAuthor(user);
        task.setPerformer(user);
        task.setTitle(title);
        task.setStatus("progress");
        task.setPriority("low");
        task.setDescription("description");
    }

    @Test
    void saveTaskPositiveTest() {
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(taskRepository.findByTitle(title)).thenReturn(null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        response = taskService.saveTask(task, username);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task);
    }

    @Test
    void saveTaskNegativeTest() {
        when(taskRepository.findByTitle(title)).thenReturn(task);
        response = taskService.saveTask(task, username);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("A task with the specified title already exists"));
    }

    @Test
    void findTask() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Task task1 = taskService.findTask(taskId);
        assertNotNull(task1);
        assertEquals(task1, task);
    }

    @Test
    void getAllTasks() {
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        List<Task> tasks1 = taskService.getAllTasks();
        assertNotNull(tasks1);
        assertEquals(tasks1, tasks);
    }

    @Test
    void getTasksByAuthor() {
        List<Task> tasks = List.of(task);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findAllByAuthor(user)).thenReturn(tasks);
        List<Task> tasks1 = taskService.getTasksByAuthor(1L);
        assertNotNull(tasks1);
        assertEquals(tasks1, tasks);
    }

    @Test
    void getTasksByPerformer() {
        List<Task> tasks = List.of(task);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findAllByPerformer(user)).thenReturn(tasks);
        List<Task> tasks1 = taskService.getTasksByPerformer(1L);
        assertNotNull(tasks1);
        assertEquals(tasks1, tasks);
    }

    @Test
    void editTaskPositiveTest() {
        when(taskRepository.findByTitle(title)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task task1 = taskRepository.findByTitle(title);
        task1.setDescription("new description");
        response = taskService.editTask(task1, username);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task1);
    }

    @Test
    void editTaskNegativeTestIncorrectTitle() {
        when(taskRepository.findByTitle(title)).thenReturn(null);
        response = taskService.editTask(task, username);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("there is no task with this title"));
    }

    @Test
    void editTaskNegativeTestNoAccess() {
        when(taskRepository.findByTitle(title)).thenReturn(task);
        response = taskService.editTask(task, "user2");
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("you cannot edit this task"));
    }

    @Test
    void editTaskStatusPositiveTest() {
        when(taskRepository.findByTitle(title)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task task1 = taskRepository.findByTitle(title);
        task1.setStatus("high");
        response = taskService.editTaskStatus(task1, username);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task1);
    }

    @Test
    void editTaskStatusNegativeTest() {
        when(taskRepository.findByTitle(title)).thenReturn(task);
        response = taskService.editTaskStatus(task, "user2");
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("you cannot edit status this task"));
    }

    @Test
    void appointAnExecutorPositiveTest() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task task1 = taskRepository.findById(taskId).get();
        User user1 = new User("user2@gmail.com", "user2", "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user1.setId(2L);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        task1.setPerformer(user1);
        response = taskService.appointAnExecutor(
                new Performer(task1.getPerformer().getUsername()), taskId, username
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task1);
    }

    @Test
    void appointAnExecutorNegativeTestIncorrectPerformer() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        User user1 = new User("user2@gmail.com", "user2", "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user1.setId(2L);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(null);
        response = taskService.appointAnExecutor(
                new Performer(user1.getUsername()),taskId, username
        );
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("that performer doesn't exist"));
    }

    @Test
    void appointAnExecutorNegativeTestNoAccess() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        User user1 = new User("user2@gmail.com", "user2", "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user1.setId(2L);
        response = taskService.appointAnExecutor(
                new Performer(user1.getUsername()), taskId, "user2"
        );
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("you cannot edit performer this task"));
    }


    @Test
    void takeTaskPositiveTest() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task task1 = taskRepository.findById(taskId).get();
        task1.setPerformer(user);
        task.setPerformer(null);
        response = taskService.takeTask(taskId, username);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task1);
    }

    @Test
    void takeTaskNegativeTest() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        response = taskService.takeTask(taskId, username);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("this task is busy"));
    }

    @Test
    void deleteTask() {
        response = taskService.deleteTask(taskId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}