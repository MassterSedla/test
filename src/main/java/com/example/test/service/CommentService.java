package com.example.test.service;

import com.example.test.model.Comment;
import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.repository.CommentRepository;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> writeComment(Long taskId, String comment, String author) {
        Task task = taskRepository.findById(taskId).get();
        User user = userRepository.findByUsername(author);
        Comment savedComment = new Comment(user, comment, task);
        return ResponseEntity.ok(commentRepository.save(savedComment));
    }

    public List<Comment> showCommentToTask(Long id) {
        return commentRepository.getAllByTask(taskRepository.findById(id).get());
    }
}
