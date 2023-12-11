package com.example.test.enums.validators;

import com.example.test.enums.TaskPriorityEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaskPriorityValidator  implements ConstraintValidator<TaskPriorityValid, String> {

    @Override
    public void initialize(TaskPriorityValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return TaskPriorityEnum.isValid(value);
    }
}