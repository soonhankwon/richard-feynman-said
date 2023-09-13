package dev.soon.richardfeynmansaid.security.service;

import dev.soon.richardfeynmansaid.security.TokenStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] decodedSecretKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedSecretKey);
    }

    public String generateToken(String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(email)
                .claim("auth", "ROLE_USER")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.key).build()
                    .parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return TokenStatus.EXPIRED;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
