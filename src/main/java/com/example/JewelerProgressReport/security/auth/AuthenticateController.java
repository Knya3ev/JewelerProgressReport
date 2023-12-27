package com.example.JewelerProgressReport.security.auth;

import com.example.JewelerProgressReport.security.auth.request.TelegramRequest;
import com.example.JewelerProgressReport.security.token.TokenService;
import com.example.JewelerProgressReport.security.token.response.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticateController {

    private final AuthenticateService authenticateService;
    private final TokenService tokenService;

    @Operation(summary = "User authorization endpoint via telegram")
    @PostMapping(value = "/telegram-auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> telegramAuth(@RequestBody @Valid TelegramRequest telegramRequest) {
        return ResponseEntity.ok(authenticateService.authentication(telegramRequest));
    }

    @Operation(summary = "jwt refresh token")
    @PostMapping(value = "/jwt-refresh")
    public ResponseEntity<JwtResponse> jwtRefresh(@RequestBody @NotBlank String refreshToken) {
        return ResponseEntity.ok(tokenService.securityTokens(refreshToken));
    }

    @Operation(summary = "check is valid token")
    @PostMapping("/valid-token")
    public ResponseEntity<Boolean> isValidToken(@RequestBody @NotBlank String token){
        return ResponseEntity.ok(authenticateService.isValidToken(token));
    }

}
