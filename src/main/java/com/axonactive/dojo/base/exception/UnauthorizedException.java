package com.axonactive.dojo.base.exception;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends AppException {

    public UnauthorizedException(String message) {
        super(Response.Status.UNAUTHORIZED.getStatusCode(),
                Response.Status.UNAUTHORIZED.getReasonPhrase(), message);
    }
}
