package com.example.JewelerProgressReport.util.validation.custom_annotation;

import com.example.JewelerProgressReport.util.validation.CustomValidationTypeMetalColor;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidationTypeMetalColor.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeMetalColor {
    String message() default "this color of the meter is incorrect or missing ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
