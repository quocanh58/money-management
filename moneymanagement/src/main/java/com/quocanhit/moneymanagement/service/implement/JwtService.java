package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.dto.JwtPropertiesDTO;
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
            // Loại bỏ "Bearer " từ token
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

//            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt);
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
        return createToken(claims, userName, 1000 * 60 * 60 * 24 * 7); // Expire in 7 days
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration();
    }

    // Create token
    public String createToken(Map<String, Object> claims, String userName, long expirationTime) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userName)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
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
