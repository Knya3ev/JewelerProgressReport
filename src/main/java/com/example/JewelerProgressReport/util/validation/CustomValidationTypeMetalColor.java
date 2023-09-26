package com.example.JewelerProgressReport.util.validation;

import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeMetalColor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.util.EnumUtils;


@RequiredArgsConstructor
public class CustomValidationTypeMetalColor implements ConstraintValidator<TypeMetalColor, String> {

    @Override
    public void initialize(TypeMetalColor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if(value == null) {
            context.buildConstraintViolationWithTemplate
                            ("Metal color cannot be empty").addConstraintViolation();
            return false;
        }

        try {

            Metal.fromCode(value);

        }catch (IllegalArgumentException e){

            context.buildConstraintViolationWithTemplate
                    (String.format("this color '%s' of the meter is incorrect or missing ",value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
