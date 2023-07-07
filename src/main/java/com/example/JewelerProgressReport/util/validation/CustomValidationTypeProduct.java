package com.example.JewelerProgressReport.util.validation;

import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfJewelry;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfMetalColor;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfOperation;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeProduct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomValidationTypeProduct implements ConstraintValidator<TypeProduct, String> {
    private List<String> validEnumList;

    @Override
    public void initialize(TypeProduct constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if(value == null) {
            context.buildConstraintViolationWithTemplate
                    ("Jewelry product type cannot be empty").addConstraintViolation();
            return false;
        }

        try {

            EnumUtils.findEnumInsensitiveCase(TypeOfJewelry.class,value);

        }catch (IllegalArgumentException e){

            context.buildConstraintViolationWithTemplate
                            (String.format("this jewelry product '%s' was not found ",value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
