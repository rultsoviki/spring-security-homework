package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.jwt")
public record JwtProperties(String secret,
                            Long accessExpirationMs,
                            Long refreshExpirationMs) {
}
