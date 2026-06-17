package com.example.demo.service;

import com.example.demo.config.JwtProperties;
import com.example.demo.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenBlackListService tokenBlackListService;
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final String TOKEN_TYPE_CLAIM = "type";

    private enum TokenTypes {
        ACCESS,
        REFRESH
    }


    public String generateAccessToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Date now = new Date();
        Date expireAt = new Date(now.getTime() + jwtProperties.accessExpirationMs() * 1000);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .claims(Map.of("roles", roles, TOKEN_TYPE_CLAIM, TokenTypes.ACCESS))
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(secretKey)
                .compact();
    }


    public String generatedRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + jwtProperties.refreshExpirationMs() * 1000);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expireAt)
                .claims(Map.of(TOKEN_TYPE_CLAIM, TokenTypes.REFRESH))
                .signWith(secretKey)
                .compact();

    }

    public void invalidateToken(String token){
        Claims claims = extractAllClaims(token);

        tokenBlackListService.blacklist(claims.getId(),claims.getExpiration());
    }

    public String extractName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isValid(token, userDetails, TokenTypes.ACCESS);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isValid(token, userDetails, TokenTypes.REFRESH);
    }

    public boolean isValid(String token, UserDetails userDetails, TokenTypes tokenTypes) {
        Claims claims = extractAllClaims(token);
        String userName = extractName(token);

        return userName.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && tokenTypes.name().equals(claims.get(TOKEN_TYPE_CLAIM, String.class))
                && !tokenBlackListService.isBlackList(claims.getId());
    }

    public List<String> extractRoles(String token) {
        var roles = extractAllClaims(token).get("roles");

        if (!(roles instanceof List<?>)) return List.of();

        return ((List<String>) roles).stream().map(String::valueOf).toList();
    }


    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
