package com.example.test.model;

import com.example.test.enums.TaskPriorityEnum;
import com.example.test.enums.TaskStatusEnum;
import com.example.test.enums.validators.TaskPriorityValid;
import com.example.test.enums.validators.TaskStatusValid;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="task_id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 5, max = 30)
    private String title;
    @Size(min = 20, max = 200)
    private String description;
    @TaskStatusValid
    private String status;
    @TaskPriorityValid
    private String priority;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "task")
    private Set<Comment> comments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author_id")
    private User author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="performer_id")
    private User performer;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getPerformer() {
        return performer;
    }

    public void setPerformer(User performer) {
        this.performer = performer;
    }
}
