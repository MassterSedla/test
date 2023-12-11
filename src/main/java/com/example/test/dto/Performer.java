package com.example.test.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Performer {
    @Size(min = 4, max = 15)
    private String username;
}
