package com.example.test.enums.validators;

import com.example.test.enums.TaskStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaskStatusValidator implements ConstraintValidator<TaskStatusValid, String> {

    @Override
    public void initialize(TaskStatusValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return TaskStatusEnum.isValid(value);
    }
}