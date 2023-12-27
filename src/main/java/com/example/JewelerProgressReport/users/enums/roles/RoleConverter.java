package com.example.JewelerProgressReport.users.enums.roles;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Set<Role>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Role::getCode)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().length() == 0) {
            return new HashSet<>();
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(Role::fromCode)
                .collect(Collectors.toSet());
    }
}
