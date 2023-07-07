package com.example.JewelerProgressReport.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldsValidationError {
    private Map<String,String > errorFieldsMessages;
}
