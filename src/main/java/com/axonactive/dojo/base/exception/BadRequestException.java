package com.axonactive.dojo.base.exception;

import lombok.Getter;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@Getter
@ApplicationException
public class BadRequestException extends  AppException{
    public BadRequestException(String message) {
        super(Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getReasonPhrase(), message);
    }
}
