package com.example.test.enums;

public enum TaskPriorityEnum {

    LOW,
    MEDIUM,
    HIGH;

    public static final String NOT_VALID_MESSAGE = "Invalid value, choose 'low', 'medium' or 'high'";

    public static boolean isValid(String value) {
        for (TaskPriorityEnum e : values()) {
            if (e.name().toLowerCase().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
