package com.axonactive.dojo.base.filter;

import com.axonactive.dojo.base.exception.UnauthorizedException;
import com.axonactive.dojo.base.jwt.payload.TokenPayload;
import com.axonactive.dojo.base.jwt.service.JwtService;
import com.axonactive.dojo.base.message.ForbiddenMessage;
import com.axonactive.dojo.enums.TokenType;
import lombok.SneakyThrows;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
@Provider
@Secure
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Inject
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext reqCtx) throws IOException {
        String token = getTokenFromHeader(reqCtx);
        TokenPayload payload = getPayload(token);

        reqCtx.setSecurityContext(new RequestSecurityContext(payload));
    }

    @SneakyThrows
    private TokenPayload getPayload(String token) {
        return jwtService.verifyToken(token, TokenType.ACCESS_TOKEN);
    }

    private String getTokenFromHeader(ContainerRequestContext reqCtx) {
        String authHeader = reqCtx.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ") ||
                authHeader.split(" ")[1].trim().isEmpty()
        ) {


            reqCtx.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .type(MediaType.APPLICATION_JSON)
                            .entity(new ForbiddenMessage())
                            .build()
            );
            return null;
        }

        return authHeader.split(" ")[1].trim();
    }
}
