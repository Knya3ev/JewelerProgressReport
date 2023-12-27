package com.example.JewelerProgressReport.shop.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequest {

    @Size(min = 3, max = 20, message = "min 3 char and max 20 char for name your shop")
    private String name;

    @Builder.Default
    private int numberOfAdministrators = 1;

    @Builder.Default
    private int numberOfShopAssistants = 2;

    @Builder.Default
    private boolean isHaveJeweler = false;

    @Builder.Default
    private int numberOfJewelerMasters = 0;

}
