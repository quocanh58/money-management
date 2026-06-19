package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.dto.auth.JwtPropertiesDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final JwtPropertiesDTO jwtPropertiesDTO;

    public JwtService(JwtPropertiesDTO jwtPropertiesDTO) {
        this.jwtPropertiesDTO = jwtPropertiesDTO;
    }

    // Validate token
    public void validateToken(final String token) {
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token or token has expired", e);
        }
    }

    // Generate access token
    public String generateToken(String userName, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userName);
        claims.put("userId", userId);
        claims.put("type", "ACCESS");
        return createToken(claims, userName, 1000 * 60 * 30); // Expire in 30 minutes
    }

    // Generate Token with Expiration
    public Map<String, Object> generateTokenWithExpiration(String userName, String userId) {
        String token = generateToken(userName, userId);
        Date expirationDate = getExpirationDateFromToken(token);
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("expirationDate", expirationDate);
        return tokenData;
    }

    // Generate refresh token (longer expiry)
    public String generateRefreshToken(String userName, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userName);
        claims.put("userId", userId);
        claims.put("type", "REFRESH");
        return createToken(claims, userName, 1000 * 60 * 60 * 24 * 7); // Expire in 7 days
    }

    // Generate new access token from refresh token
    public String refreshAccessToken(String refreshToken) {
        if (isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token has expired");
        }

        Claims claims = extractClaims(refreshToken);

        String type = claims.get("type", String.class);
        if (!"REFRESH".equals(type)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = claims.get("username", String.class);
        String userId = claims.get("userId", String.class);

        return generateToken(username, userId);
    }

    public Map<String, Object> getUserNameByToken(String token) {
        Claims claims = extractClaims(token);
        String type = claims.get("type", String.class);
        String username = claims.get("username", String.class);
        String userId = claims.get("userId", String.class);
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("username", username);
        data.put("type", type);
        return data;
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration();
    }

    // Create token
    public String createToken(Map<String, Object> claims, String userName, long expirationTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey())
                .compact();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Token invalid or expired
        }
    }

    // Extract claims from token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Get signing key
    public SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtPropertiesDTO.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
