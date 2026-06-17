package com.mprribeiro.app_ai_crafter.security;

import com.mprribeiro.app_ai_crafter.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Component
public class AuthUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10)) // Token válido por 10 minutos
                .signWith(getSecretKey())
                .compact();
    }

    public JwtUserPrincipal verifyToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        Long userId = Long.parseLong(claims.get("userId", String.class));

        return new JwtUserPrincipal(userId, username, new ArrayList<>());
    }

    public Long getCurrentUserId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT found!");
        }

        return ((JwtUserPrincipal) authentication.getPrincipal()).userId();
    }
}
