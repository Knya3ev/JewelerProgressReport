package com.example.JewelerProgressReport.shop.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponseCountModerationReports {
    private Long id;
    private String name;
    private int moderation;
}
