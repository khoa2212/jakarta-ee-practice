package com.axonactive.dojo.base.filter;

import com.axonactive.dojo.base.exception.content.ExceptionContent;
import com.axonactive.dojo.base.exception.ForbiddenException;
import com.axonactive.dojo.base.exception.UnauthorizedException;
import lombok.SneakyThrows;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo info;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext reqCtx) throws IOException {

        RolesAllowed methodRoles = info.getResourceMethod().getAnnotation(RolesAllowed.class);
        RolesAllowed classRoles = info.getResourceClass().getAnnotation(RolesAllowed.class);

        if (methodRoles == null && classRoles == null) {
            return;
        }

        if (!isLogin()) {
            reqCtx.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(ExceptionContent
                            .builder()
                            .errorKey(Response.Status.UNAUTHORIZED.getReasonPhrase())
                            .success(false)
                            .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
                            .message("Invalid token")
                            .build())
                    .build());
            return;
        }

        if (isNotAllowed(methodRoles)) {
            reqCtx.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(ExceptionContent
                            .builder()
                            .errorKey(Response.Status.FORBIDDEN.getReasonPhrase())
                            .success(false)
                            .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                            .message("Not Allowed")
                            .build())
                    .build());
        }
    }

    @SneakyThrows
    public boolean isLogin() {
        String user = securityContext.getUserPrincipal().getName();
        if (user == null) {
            throw new UnauthorizedException("Invalid token");
        }
        return true;
    }

    @SneakyThrows
    public boolean isNotAllowed(RolesAllowed anno) {
        if (anno == null) {
            return false;
        }

        String[] roles = anno.value();

        for (String role : roles) {
            if (securityContext.isUserInRole(role)) {
                return false;
            }
        }

        throw new ForbiddenException("Not Allowed");
    }
}
