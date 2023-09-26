package com.example.JewelerProgressReport.jewelry.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MetalConverter implements AttributeConverter<Metal, String> {
    @Override
    public String convertToDatabaseColumn(Metal metal) {
        if (metal == null) {
            return null;
        }
        return metal.getCode();
    }

    @Override
    public Metal convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Metal.fromCode(code);
    }
}
