package com.example.test.enums;


import java.util.Locale;

public enum TaskStatusEnum {
    PENDING,
    PROGRESS,
    COMPLETED;

    public static final String NOT_VALID_MESSAGE = "Invalid value, choose 'pending', 'progress' or 'completed'";

    public static boolean isValid(String value) {
        for (TaskStatusEnum e : values()) {
            if (e.name().toLowerCase().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
