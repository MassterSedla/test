package com.example.test.repository;

import com.example.test.model.Task;
import com.example.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthor(User user);
    List<Task> findAllByPerformer(User user);
    Task findByTitle(String title);
}
