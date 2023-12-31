package com.example.JewelerProgressReport.users.user.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private Long telegramId;
    private String username;
    private String firstname;
    private String phoneNumber;
    private List<String> roles;
}
