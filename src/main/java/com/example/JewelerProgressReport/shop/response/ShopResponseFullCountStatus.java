package com.example.JewelerProgressReport.shop.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ShopResponseFullCountStatus {
    private Long id;
    private String name;
    private long all;
    private int moderation;
    private int uniqueness;
    private int ordinary;
    private int rejection;
}
