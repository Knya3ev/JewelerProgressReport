package com.example.JewelerProgressReport.shop.response;

import com.example.JewelerProgressReport.users.user.response.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ShopResponse {
    private String name;

    private UserResponse director;

    private List<UserResponse> administrators;
    private List<UserResponse> shopAssistants;

    private boolean isHaveJeweler;
    private List<UserResponse> jewelerMasters;

    private int subscriptionDays ;
}
