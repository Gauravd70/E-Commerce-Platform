package com.gauravd70.commons.security;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${jwt.secret:DEFAULT_KEY}")
    private String jwtSecret;

    @Value("${jwt.expiry.access:360000}")
    private long accessTokenExpiryInMs;

    @Value("${jwt.expiry.refresh:60480000}")
    private long refreshTokenExpiryInMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    
    private String create(String userId, Map<String, Object> claims, long expiryInMs) {
        return Jwts.builder()
            .setSubject(userId)
            .addClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiryInMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Generate a new JWT access token
     * 
     * @param userId {@link String}
     * @param claims {@link Map}
     * @param type {@link JwtType}
     * @return {@link String}
     */
    public String create(JwtType type, String userId, Map<String, Object> claims) {
        if(JwtType.REFRESH_TOKEN.equals(type)) {
            return create(userId, claims, refreshTokenExpiryInMs);
        }

        return create(userId, claims, accessTokenExpiryInMs);
    }

    /**
     * Get a JWT cookie based on the type
     * 
     * @param key {@link String}
     * @param claims {@link Map}
     * @param type {@link JwtType}
     * @return
     */
    public ResponseCookie createCookie(JwtType type, String userId, Map<String, Object> claims) {
        String jwt = create(type, userId, claims);

        if(JwtType.REFRESH_TOKEN.equals(type)) {
            return ResponseCookie.from(JwtType.REFRESH_TOKEN.name(), jwt).httpOnly(true).path("/").maxAge(refreshTokenExpiryInMs).build();
        }

        return ResponseCookie.from(JwtType.ACCESS_TOKEN.name(), jwt).httpOnly(true).path("/").maxAge(accessTokenExpiryInMs).build();
    }

    /**
     * Get a JWT cookie based on the type
     * 
     * @param key {@link String}
     * @param claims {@link Map}
     * @param type {@link JwtType}
     * @return
     */
    public ResponseCookie invalidateCookie(JwtType type) {
        return ResponseCookie.from(type.name(), "").httpOnly(true).path("/").maxAge(0).build();
    }

    /**
     * Parse the jwt token
     * 
     * @param jwt {@link String}
     * @return {@link Claims}
     * @throws JwtException
     * @throws SignatureException
     * @throws ExpiredJwtException
     * @throws IllegalArgumentException
     */
    public Claims getClaims(String jwt) throws JwtException, SignatureException, ExpiredJwtException, IllegalArgumentException {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }
}
