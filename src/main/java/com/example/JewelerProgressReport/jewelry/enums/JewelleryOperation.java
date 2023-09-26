package com.example.JewelerProgressReport.jewelry.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum JewelleryOperation {
    SIZE_ADJUSTMENT("SZAD", "корректировка размера"),
    ADD_GOLD ("ADGD", "с добавлением золота"),
    CLEANING ("CLG", "чистка"),
    POLISHING ("PLSH", "полировка"),
    RHODIUM_PLATING ("RHPG", "родирование"),
    DIAMOND_INLAY ("DMIL", "инкрустация бриллианта"),
    REPAIR ("RER", "ремонт"),
    UPGRADE ("UPGD", "добавление элементов");

    private final String code;
    private final String ru;

    JewelleryOperation(String code, String ru) {
        this.code = code;
        this.ru = ru;
    }

    public String getCode() {
        return this.code;
    }

    public String getRu() {
        return this.ru;
    }

    public static List<JewelleryOperation> getStandardOperation(String metalColor) {
        Metal metal = Metal.fromCode(metalColor);

        if (metal == Metal.WHITE_GOLD || metal == Metal.SILVER) {
            return new ArrayList<>(Arrays.asList(CLEANING, POLISHING, RHODIUM_PLATING));
        } else {
            return new ArrayList<>(Arrays.asList(CLEANING, POLISHING));
        }
    }

    public static JewelleryOperation fromCode(String code) {
        return Stream.of(JewelleryOperation.values())
                .filter(i -> i.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}