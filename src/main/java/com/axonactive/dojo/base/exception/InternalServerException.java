package com.axonactive.dojo.base.exception;

import javax.ws.rs.core.Response;

public class InternalServerException extends AppException {
    public InternalServerException(String message) {
        super(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), message);
    }
}
