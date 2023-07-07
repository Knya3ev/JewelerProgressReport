package com.example.JewelerProgressReport.model.typeEnum;

public enum TypeOfMetalColor {
    yellow("лимонное", "yellow"),
    red("красное", "red"),
    white("белое", "white");

    private final String metalColor;
    private final String metalColorEn;

    TypeOfMetalColor(String metalColor, String metalColorEn) {
        this.metalColor = metalColor;
        this.metalColorEn = metalColorEn;
    }

    public String get() {
        return this.metalColorEn;
    }
    public String getRu(){
        return this.metalColor;
    }
}
