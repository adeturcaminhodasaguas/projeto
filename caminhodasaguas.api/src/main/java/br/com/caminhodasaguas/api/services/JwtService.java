package br.com.caminhodasaguas.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.caminhodasaguas.api.domains.UserDomain;
import br.com.caminhodasaguas.api.domains.enums.UserEnum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration_client}")
    private long expirationClient;

    @Value("${jwt.expiration_admin}")
    private long expirationAdmin;


    public String generateToken(UserDomain user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            long expiration = user.getRole().equals(UserEnum.ADMIN) ? expirationAdmin : expirationClient;

            return JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getId().toString())
                    .withClaim("id", user.getId().toString())
                    .withClaim("name", user.getName())
                    .withClaim("email", user.getEmail())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(generateExpirationDate(expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro durante a autenticação.");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate(long expiration){
        return LocalDateTime.now().plusDays(expiration).toInstant(ZoneOffset.of("-03:00"));
    }
}
