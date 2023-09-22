package com.example.JewelerProgressReport.jewelry.enums;

public enum TypeOfOperation {
    sizeAdjustment("корректировка размера", "sizeAdjustment"),
    addGold("с добавлением золота", "addGold"),
    cleaning("чистка", "cleaning"),
    polishing("полировка", "polishing"),
    rhodiumPlating("родирование", "rhodiumPlating"),
    diamondInlay("инкрустация бриллианта", "diamondInlay"),
    repair("ремонт", "repair"),
    upgrade("добавление элементов", "upgrade");

    private final String operation;
    private final String operationEn;

    TypeOfOperation(String operation, String operationEn) {
        this.operation = operation;
        this.operationEn = operationEn;
    }

    public String get() {
        return this.operationEn;
    }

    public String getRu() {
        return this.operation;
    }

}
