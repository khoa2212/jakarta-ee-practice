package com.axonactive.dojo.base.exception;

import lombok.Getter;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@Getter
@ApplicationException
public class EntityNotFoundException extends AppException {

    public EntityNotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(), message);
    }
}
