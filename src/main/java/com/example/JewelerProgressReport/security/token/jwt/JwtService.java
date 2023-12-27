package com.example.JewelerProgressReport.security.token.jwt;

import com.example.JewelerProgressReport.users.enums.roles.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${settings.jwt.secret}")
    private String accessSecret;

    @Value("${settings.jwt.refresh.token}")
    private String refreshSecret;

    @Value("${settings.jwt.expiration-minutes}")
    private long expiration;


    public UsernamePasswordAuthenticationToken getAuthentication(String token) throws JwtException {
        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        Set<Role> authorities = roles.stream().map(Role::valueOf).collect(Collectors.toSet());

        UserDetails userDetails = User
                .withUsername(username)
                .authorities(authorities)
                .password("")
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private String generateAccessToken(Map<String,Object> extraClaims, UserDetails userDetails){
        String[] roles =userDetails.getAuthorities().stream().map(grantedAuthority -> {
            Role role = (Role) grantedAuthority;
            return role.name();
        }).toList().toArray(new String[0]);
        extraClaims.put("roles", roles);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expiration)))
                .signWith(getSigningKey(accessSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(getSigningKey(refreshSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    private Key getSigningKey(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(token);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey(accessSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    public static String resolveToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
