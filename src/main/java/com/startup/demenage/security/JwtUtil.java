package com.startup.demenage.security;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("demenage")
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // Handle token verification exception
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String extractUsername(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public String extractRole(DecodedJWT jwt) {
        return jwt.getClaim("roles").toString();
    }
    
}
