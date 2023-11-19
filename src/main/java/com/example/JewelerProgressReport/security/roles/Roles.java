package com.example.JewelerProgressReport.security.roles;


public enum Roles {
    ROLE_USER("USR"),
    ROLE_ADMIN("ADM"),
    ROLE_JEWELER("JWR"),
    ROLE_SHOP_ASSISTANT("SAT"),
    ROLE_DIRECTOR("DCR"),
    ROLE_GUEST("GST");

    private String code;

    Roles(String code){
        this.code = code;
    }

    public String get(){
        return code;
    }
}
