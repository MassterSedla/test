package com.example.test.controller;

import com.example.test.dto.CommentDto;
import com.example.test.model.Comment;
import com.example.test.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{taskId}/comment")
public class CommentController {
    private final CommentService service;

    @GetMapping
    public ResponseEntity<List<Comment>> showCommentToTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(service.showCommentToTask(taskId));
    }

    @PostMapping
    public ResponseEntity<Comment> writeComment(@PathVariable Long taskId,
                                          @Valid @RequestBody CommentDto comment,
                                          Principal principal) {
        return ResponseEntity.ok(
                service.writeComment(taskId,
                        comment.getComment(), principal.getName()
                )
        );
    }
}
