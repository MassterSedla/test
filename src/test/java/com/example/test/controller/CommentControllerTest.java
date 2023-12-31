package com.example.test.controller;

import com.example.test.config.JwtRequestFilter;
import com.example.test.model.Comment;
import com.example.test.model.Role;
import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.service.AuthService;
import com.example.test.service.CommentService;
import com.example.test.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private JwtUtil jwtUtil;
    private Long taskId;
    private String comment;
    private String author;
    private User user;
    private Task task;
    private Comment commentEx;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CommentController(commentService))
                .addFilters(jwtRequestFilter)
                .build();

        taskId = 1L;
        comment = "comment";
        author = "user";
        user = new User("user@gamil.com", author, "user",
                Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setId(taskId);
        task = new Task();
        task.setId(1L);
        task.setAuthor(user);
        task.setPerformer(user);
        task.setTitle("title");
        task.setStatus("progress");
        task.setPriority("low");
        task.setDescription("description");
        commentEx = new Comment(user, comment, task);
    }

    @Test
    void showCommentToTask() throws Exception {
        List<Comment> comments = List.of(commentEx);

        when(commentService.showCommentToTask(taskId)).thenReturn(comments);
        mockMvc.perform(get("/api/tasks/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskId)))
                .andExpect(status().isOk());
        verify(commentService).showCommentToTask(taskId);
    }

    @Test
    void writeComment() throws Exception {
        String token = jwtUtil.generatedToken(user);
        Authentication auht = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                jwtUtil.getRoleFromToken(token).stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(auht);

        when(commentService.writeComment(taskId, comment, author)).thenReturn(commentEx);
        mockMvc.perform(post("/api/tasks/1/comment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentEx)))
                .andExpect(status().isOk());
        verify(commentService).writeComment(taskId, comment, author);
    }
}