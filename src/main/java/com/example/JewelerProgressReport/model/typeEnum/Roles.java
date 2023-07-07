package com.example.JewelerProgressReport.model.typeEnum;


public enum Roles {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String name;

    Roles(String name){
        this.name = name;
    }

    public String get(){
        return name;
    }
}
