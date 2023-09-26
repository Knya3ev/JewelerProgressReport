package com.example.JewelerProgressReport.jewelry.enums;

public enum Metal {
    YELLOW_GOLD("YGD", "желтое золото"),
    RED_GOLD("RGD", "красное золото"),
    WHITE_GOLD("WGD", "белое золото"),
    SILVER("SVR","серебро");

    private final String code;
    private final String ru;

    Metal(String code, String ru) {
        this.code = code;
        this.ru = ru;
    }

    public String getCode() {
        return this.code;
    }
    public String getRu(){
        return this.ru;
    }

    public static Metal fromCode(String code){
        for (Metal metal : Metal.values()){
            if(metal.getCode().equals(code)) return metal;
        }
        throw new IllegalArgumentException("Неизвестный код металла:" + code);
    }
}
