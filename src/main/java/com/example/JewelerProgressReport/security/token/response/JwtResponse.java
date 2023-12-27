package com.example.JewelerProgressReport.security.token.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

    private String jwt;
    private String refreshToken;

}
