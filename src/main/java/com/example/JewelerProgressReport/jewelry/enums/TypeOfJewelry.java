package com.example.JewelerProgressReport.jewelry.enums;

public enum TypeOfJewelry {
    ring("кольцо", "ring"),
    earrings("серьги", "earrings"),
    bracelet("браслет", "bracelet"),
    suspension("подвеска", "suspension"),
    chain("цепочка", "chain");

    private final String typeProduct;
    private final String typeProductEn;

    TypeOfJewelry(String typeProduct, String typeProductEn) {
        this.typeProduct = typeProduct;
        this.typeProductEn = typeProductEn;
    }

    public String get() {
        return this.typeProductEn;
    }
    public String getRu(){
        return this.typeProduct;
    }
}
