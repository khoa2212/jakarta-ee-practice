package com.axonactive.dojo.base.exception;

import lombok.Getter;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@Getter
@ApplicationException
public class EntityNotFoundException extends AppException {

    public EntityNotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(), message);
    }
}
