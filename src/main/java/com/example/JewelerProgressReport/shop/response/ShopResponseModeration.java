package com.example.JewelerProgressReport.shop.response;

import com.example.JewelerProgressReport.users.user.response.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopResponseModeration {
    private Long id;
    private String name;
    private boolean isHaveJeweler;
    private UserResponse creator;
}
