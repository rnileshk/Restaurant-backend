package com.restaurant.app.security;

import com.restaurant.app.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    /*
     =====================================
     GENERATE TOKEN
     =====================================
    */

    public String generateToken(
            User user
    ) {

        Map<String, Object> claims =
                new HashMap<>();

        claims.put(
                "role",
                user.getRole().name()
        );

        claims.put(
                "name",
                user.getName()
        );

        return Jwts.builder()

                .setClaims(claims)

                .setSubject(user.getEmail())

                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )

                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    /*
     =====================================
     EXTRACT EMAIL
     =====================================
    */

    public String extractEmail(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    /*
     =====================================
     VALIDATE TOKEN
     =====================================
    */

    public boolean isTokenValid(
            String token,
            User user
    ) {

        final String email =
                extractEmail(token);

        return email.equals(user.getEmail())
                && !isTokenExpired(token);
    }

    /*
     =====================================
     CHECK EXPIRATION
     =====================================
    */

    private boolean isTokenExpired(
            String token
    ) {

        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    /*
     =====================================
     EXTRACT CLAIM
     =====================================
    */

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        final Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);
    }

    /*
     =====================================
     EXTRACT ALL CLAIMS
     =====================================
    */

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        getSignInKey()
                )

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    /*
     =====================================
     SECRET KEY
     =====================================
    */

    private Key getSignInKey() {

        byte[] keyBytes;

        try {

            keyBytes =
                    Decoders.BASE64.decode(secretKey);

        } catch (Exception e) {

            keyBytes =
                    secretKey.getBytes();
        }

        return Keys.hmacShaKeyFor(
                keyBytes
        );
    }
}