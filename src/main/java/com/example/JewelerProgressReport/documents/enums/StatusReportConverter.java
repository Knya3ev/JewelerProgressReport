package com.example.JewelerProgressReport.documents.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusReportConverter implements AttributeConverter<StatusReport, String> {

    @Override
    public String convertToDatabaseColumn(StatusReport status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public StatusReport convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return StatusReport.fromCode(code);
    }
}
