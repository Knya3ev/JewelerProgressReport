package com.example.JewelerProgressReport.util.validation;

import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeProduct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.yaml.snakeyaml.util.EnumUtils;

public class CustomValidationTypeProduct implements ConstraintValidator<TypeProduct, String> {

    @Override
    public void initialize(TypeProduct constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (value == null) {
            context.buildConstraintViolationWithTemplate
                    ("Jewelry product type cannot be empty").addConstraintViolation();
            return false;
        }

        try {

            JewelleryProduct.fromCode(value);

        } catch (IllegalArgumentException e) {

            context.buildConstraintViolationWithTemplate
                            (String.format("this jewelry product '%s' was not found ", value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
