package com.example.JewelerProgressReport.documents.enums;

public enum StatusReport {
    MODERATION("MDR"),
    UNIQUE("UNQ"),
    ORDINARY("ORY"),
    REJECTION("RTN"),
    CONSULTATION("CTN"),
    SERVICE("SRC");
    private final String code;

    StatusReport(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static StatusReport fromCode(String code){
        for (StatusReport status : StatusReport.values()){
            if(status.getCode().equals(code)) return status;
        }
        throw new IllegalArgumentException("Неизвестный код статуса:" + code);
    }
}
