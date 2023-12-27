package com.example.JewelerProgressReport.shop.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopShortResponse {
    private Long id;
    private String name;
}
