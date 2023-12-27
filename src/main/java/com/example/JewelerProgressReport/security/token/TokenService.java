package com.example.JewelerProgressReport.security.token;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.security.token.jwt.JwtService;
import com.example.JewelerProgressReport.security.token.response.JwtResponse;
import com.example.JewelerProgressReport.users.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    @Value("${settings.jwt.refresh.expiration-days}")
    private long expiration;

    @Transactional
    public JwtResponse securityTokens(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new HttpException("Invalid refresh token", HttpStatus.BAD_REQUEST));

        Date now = new Date();

        if (token.getTokenRefresh().equals(refreshToken) && token.getExpiration().after(now)) {
            updateJwtToken(token);
            return new JwtResponse(token.getJwt(), token.getTokenRefresh());
        }

        throw new HttpException("Refresh token expired", HttpStatus.BAD_REQUEST);
    }


    @Transactional
    public JwtResponse createUserToken(User user) {
        Optional<Token> request = tokenRepository.findByUser(user.getId());
        Token token = request.map(this::updateToken).orElseGet(() -> updateToken(user));

        return new JwtResponse(token.getJwt(), token.getTokenRefresh());
    }


    private Token updateToken(User user) {
        return tokenRepository.save(
                Token.builder()
                        .jwt(jwtService.generateToken(user))
                        .tokenRefresh(jwtService.generateRefreshToken(user))
                        .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(expiration)))
                        .user(user)
                        .build());
    }

    private Token updateToken(Token token) {
        User user = token.getUser();
        token.setJwt(jwtService.generateToken(user));
        token.setTokenRefresh(jwtService.generateRefreshToken(user));
        token.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(expiration)));
        return token;
    }

    private void updateJwtToken(Token token) {
        token.setTokenRefresh(jwtService.generateRefreshToken(token.getUser()));

        if (!jwtService.isTokenValid(token.getJwt())) {
            User user = token.getUser();
            String jwt = jwtService.generateToken(user);
            token.setJwt(jwt);
        }
    }

}
