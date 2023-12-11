package com.example.test.enums.validators;

import com.example.test.enums.TaskPriorityEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.example.test.enums.TaskPriorityEnum.NOT_VALID_MESSAGE;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = TaskPriorityValidator.class)
public @interface TaskPriorityValid {
    String message() default NOT_VALID_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
