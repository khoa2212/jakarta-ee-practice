package com.axonactive.dojo.base.filter;

import com.axonactive.dojo.base.exception.content.ExceptionContent;
import com.axonactive.dojo.base.exception.ForbiddenException;
import com.axonactive.dojo.base.exception.UnauthorizedException;
import lombok.SneakyThrows;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo info;

    @Context
    private SecurityContext securityContext;

    @Override
    @SneakyThrows
    public void filter(ContainerRequestContext reqCtx) throws IOException {

        RolesAllowed methodRoles = info.getResourceMethod().getAnnotation(RolesAllowed.class);

        if (methodRoles == null) {
            return;
        }

        if(!isLogin()) {
            throw new UnauthorizedException("Invalid token");
        }

        if(isNotAllowed(methodRoles)) {
            throw new ForbiddenException("Not Allowed");
        }
    }

    public boolean isLogin() {
        Principal user = securityContext.getUserPrincipal();
        if (user == null) {
           return false;
        }
        return true;
    }

    public boolean isNotAllowed(RolesAllowed anno) {
        if (anno == null) {
            return false;
        }

        String[] roles = anno.value();

        for (String role : roles) {
            if(securityContext.isUserInRole(role)) {
                return false;
            }
        }

        return true;
    }
}
