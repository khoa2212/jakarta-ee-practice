package com.axonactive.dojo.base.exception;

import lombok.Getter;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@Getter
@ApplicationException
public class ForbiddenException extends AppException {
    public ForbiddenException(String message) {
        super(Response.Status.FORBIDDEN.getStatusCode(),
                Response.Status.FORBIDDEN.getReasonPhrase(), message);
    }
}
