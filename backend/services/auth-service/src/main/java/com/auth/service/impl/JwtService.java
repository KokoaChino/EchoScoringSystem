package com.auth.service.impl;

import com.common.constant.Constant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class JwtService {

    private static final long DEFAULT_EXPIRATION = 30L * 24 * 60 * 60 * 1000;

    public String generateToken(String username, Integer userId, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(Constant.SECRET_BYTES), SignatureAlgorithm.HS256)
                .compact();
    }
}