package com.ecommerce_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// @Component means Spring will manage this class automatically
// We can inject it anywhere using @Autowired
@Component
public class JwtUtil {

    // Reads value from application.properties
    // app.jwt.secret=ecommerce_super_secret_key_minimum_32_chars_long
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // app.jwt.expiration=86400000 (24 hours in milliseconds)
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // Creates a signing key from our secret string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generates a JWT token for the given email
    // Called after successful login
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)           // stores email inside token
                .setIssuedAt(new Date())      // token created at this time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // expires after 24hrs
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with our secret
                .compact();                  // build the token string
    }

    // Extracts email from token
    // Called on every request to know who is making the request
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Checks if token is valid and not expired
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date()); // check expiry
        } catch (Exception e) {
            return false; // invalid token
        }
    }

    // Reads all data stored inside the token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}