package com.pedro.f20.services;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pedro.f20.entities.User;

@Service
public class TokenService {
    public String generateToken(User user){
        try{
            var algorithm = Algorithm.HMAC256("12345678");
            return JWT.create()
                    .withIssuer("API F20")
                    .withSubject(user.getEmail())
                    .withClaim("name", user.getUsername())
                    .sign(algorithm);

        } catch(JWTCreationException exception){
            throw new RuntimeException("ERROR IN TOKEN GENERATE", exception);
        }
    }

    public String getSubject(String tokenJWT){
        try{
            var algorithm = Algorithm.HMAC256("12345678");
            return JWT.require(algorithm)
                    .withIssuer("API F20")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch(JWTVerificationException exception){
            throw new RuntimeException("Token JWT is not valid.");
        }
    }
}