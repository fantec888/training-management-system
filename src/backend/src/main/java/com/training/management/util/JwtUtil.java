package com.training.management.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import com.training.management.domain.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Integer expireHours;

    public JwtUtil(
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.expire-hours}") Integer expireHours
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireHours = expireHours;
    }

    public String generateToken(SysUser user) {
        Date now = new Date();
        Date expireAt = Date.from(
            LocalDateTime.now()
                .plusHours(expireHours)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );

        return Jwts.builder()
            .subject(user.getUsername())
            .claim("uid", user.getId())
            .claim("roleCode", user.getRoleCode())
            .issuedAt(now)
            .expiration(expireAt)
            .signWith(secretKey)
            .compact();
    }

    public String parseUsername(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.getSubject();
    }
}
