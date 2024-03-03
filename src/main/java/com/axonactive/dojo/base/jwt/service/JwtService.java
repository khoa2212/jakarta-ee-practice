package com.axonactive.dojo.base.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.axonactive.dojo.base.config.JwtConfig;
import com.axonactive.dojo.base.exception.UnauthorizedException;
import com.axonactive.dojo.base.jwt.payload.TokenPayload;
import com.axonactive.dojo.enums.Role;
import com.axonactive.dojo.enums.TokenType;

import javax.ejb.Stateless;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Stateless
public class JwtService {
    private static final String CREATE_JWT_FAILED = "JWT creation failed";
    private static final String INVALID_TOKEN = "Invalid token";
    private final Algorithm algorithm = Algorithm.HMAC256(JwtConfig.getSecretKey());
    public String generateToken(TokenPayload payload) {
        String token;

        try {
            Map<String, String> map = new HashMap<>();

            map.put("tokenType", payload.getTokenType().toString());
            map.put("email", payload.getEmail());
            map.put("displayName", payload.getDisplayName());
            map.put("role", payload.getRole().toString());

            JWTCreator.Builder jwtBuilder = JWT.create();
            jwtBuilder.withPayload(map);
            jwtBuilder.withIssuer(JwtConfig.getIssuer());
            jwtBuilder.withExpiresAt(new Date(System.currentTimeMillis() + JwtConfig.getAccessTokenTimeToLive()));

            token = jwtBuilder.sign(algorithm);
        } catch (JWTCreationException exception){
            throw new ServerErrorException(CREATE_JWT_FAILED, Response.Status.INTERNAL_SERVER_ERROR, exception);
        }

        return token;
    }

    public TokenPayload verifyToken(String token, TokenType type) throws UnauthorizedException {
        DecodedJWT decodedJWT;

        try {
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(JwtConfig.getIssuer()).build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }

        Map<String, String> map = new HashMap<String, String>();
        for (var key : decodedJWT.getClaims().keySet()) {
            map.put(key, decodedJWT.getClaim(key).asString());
        }

        if(!Objects.equals(map.get("tokenType"), type.toString())) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }

        return TokenPayload.builder()
                .tokenType(TokenType.valueOf(map.get("tokenType")))
                .displayName(map.get("displayName"))
                .email(map.get("email"))
                .role(Role.valueOf(map.get("role")))
                .build();
    }
}
