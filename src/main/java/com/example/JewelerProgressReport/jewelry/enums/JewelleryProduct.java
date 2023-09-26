package com.example.JewelerProgressReport.jewelry.enums;

public enum JewelleryProduct {
    RING("RNG", "кольцо"),
    EARRINGS("ERN", "серьги"),
    BRACELET("BRT", "браслет"),
    SUSPENSION("SPN", "подвеска"),
    CHAIN("CHN", "цепочка");

    private final String code;
    private final String ru;

    JewelleryProduct(String code, String ru) {
        this.code = code;
        this.ru = ru;
    }

    public String getCode() {
        return this.code;
    }
    public String getRu(){
        return this.ru;
    }

    public static JewelleryProduct fromCode(String code){
        for (JewelleryProduct jewelleryProduct : JewelleryProduct.values()){
            if(jewelleryProduct.getCode().equals(code)) return jewelleryProduct;
        }
        throw new IllegalArgumentException("Неизвестный код украшения:" + code);
    }
}
