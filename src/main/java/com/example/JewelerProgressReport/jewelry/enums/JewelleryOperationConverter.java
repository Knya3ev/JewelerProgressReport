package com.example.JewelerProgressReport.jewelry.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class JewelleryOperationConverter implements AttributeConverter<List<JewelleryOperation>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<JewelleryOperation> attribute) {
        if (attribute == null) {
            return null;
        }
        if (attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(JewelleryOperation::getCode)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<JewelleryOperation> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if (dbData.trim().length() == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(JewelleryOperation::fromCode)
                .collect(Collectors.toList());
    }
}
