package com.example.test.service;

import com.example.test.model.Comment;
import com.example.test.model.Role;
import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.repository.CommentRepository;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentService commentService;
    private String username;
    private Long taskId;
    private String commentString;
    private User user;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        username = "user";
        taskId = 1L;
        commentString = "comment";
        user = new User("user@gmail.com", username, "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setId(1L);
        task = new Task();
        task.setId(taskId);
        task.setAuthor(user);
        task.setTitle("title");
        task.setStatus("progress");
        task.setPriority("low");
        task.setDescription("description");
        comment = new Comment(user, commentString, task);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    }

    @Test
    void writeCommentPositiveTest() {
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment comment1 =
                commentService.writeComment(taskId, commentString, username);
        assertNotNull(comment1);
        assertEquals(comment1, comment);

    }

    @Test
    void showCommentToTask() {
        List<Comment> comments = List.of(comment);
        when(commentRepository.getAllByTask(task)).thenReturn(comments);
        List<Comment> comments1 = commentService.showCommentToTask(taskId);
        assertNotNull(comments1);
        assertEquals(comments1, comments);
    }
}