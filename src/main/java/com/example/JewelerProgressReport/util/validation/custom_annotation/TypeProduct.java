package com.example.JewelerProgressReport.util.validation.custom_annotation;


import com.example.JewelerProgressReport.util.validation.CustomValidationTypeOperation;
import com.example.JewelerProgressReport.util.validation.CustomValidationTypeProduct;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidationTypeProduct.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeProduct {
    String message() default "this jewelry operation was not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
