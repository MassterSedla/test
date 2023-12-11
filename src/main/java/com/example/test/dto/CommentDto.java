package com.example.test.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    @Size(min = 5, max = 150)
    String comment;
}
