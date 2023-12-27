package com.example.JewelerProgressReport.security.auth;

import com.example.JewelerProgressReport.security.auth.request.TelegramRequest;
import com.example.JewelerProgressReport.security.token.TokenService;
import com.example.JewelerProgressReport.security.token.jwt.JwtService;
import com.example.JewelerProgressReport.security.token.response.JwtResponse;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.users.user.UserRepository;
import com.example.JewelerProgressReport.users.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;


    @Transactional
    public JwtResponse authentication(TelegramRequest telegramRequest) {
        User user = userService.getUser(telegramRequest);

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authReq);

        return tokenService.createUserToken(user);
    }

    public boolean isValidToken(String token){
        return jwtService.isTokenValid(token);
    }

}
