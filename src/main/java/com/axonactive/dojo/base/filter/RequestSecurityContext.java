package com.axonactive.dojo.base.filter;

import com.axonactive.dojo.base.jwt.payload.TokenPayload;
import lombok.AllArgsConstructor;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@AllArgsConstructor
public class RequestSecurityContext implements SecurityContext {
    private TokenPayload payload;

    @Override
    public Principal getUserPrincipal() {
        return payload;
    }

    @Override
    public boolean isUserInRole(String s) {
        return payload.getRole().toString().trim().equals(s);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
