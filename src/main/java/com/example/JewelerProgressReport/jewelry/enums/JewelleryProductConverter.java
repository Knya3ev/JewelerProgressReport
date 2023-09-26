package com.example.JewelerProgressReport.jewelry.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JewelleryProductConverter implements AttributeConverter<JewelleryProduct, String> {
    @Override
    public String convertToDatabaseColumn(JewelleryProduct jewelleryProduct) {
        if (jewelleryProduct == null) {
            return null;
        }
        return jewelleryProduct.getCode();
    }

    @Override
    public JewelleryProduct convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return JewelleryProduct.fromCode(code);
    }
}
