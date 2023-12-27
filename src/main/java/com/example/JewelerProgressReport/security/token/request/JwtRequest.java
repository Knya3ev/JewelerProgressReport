package com.example.JewelerProgressReport.security.token.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {

    @NotBlank(message = "Login must not be blank")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
