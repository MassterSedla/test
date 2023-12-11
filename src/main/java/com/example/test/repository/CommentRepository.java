package com.example.test.repository;

import com.example.test.model.Comment;
import com.example.test.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByTask(Task task);
}
