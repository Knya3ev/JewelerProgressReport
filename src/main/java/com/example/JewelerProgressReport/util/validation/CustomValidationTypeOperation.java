package com.example.JewelerProgressReport.util.validation;

import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfMetalColor;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfOperation;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeMetalColor;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeOperation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.List;

public class CustomValidationTypeOperation implements ConstraintValidator<TypeOperation, List<String>> {
    @Override
    public void initialize(TypeOperation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (value.isEmpty()) {
            context.buildConstraintViolationWithTemplate
                    ("Operation cannot be empty").addConstraintViolation();
            return false;
        }
        int i = 0;
        try {
            for (String item : value) {
                EnumUtils.findEnumInsensitiveCase(TypeOfOperation.class, item);
                i++;
            }
        } catch (IllegalArgumentException e) {

            context.buildConstraintViolationWithTemplate
                            (String.format("jewelry operations [%s] was not found", value.get(i)))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
