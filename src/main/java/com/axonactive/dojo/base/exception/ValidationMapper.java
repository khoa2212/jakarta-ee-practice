package com.axonactive.dojo.base.exception;

import com.axonactive.dojo.base.entity.ExceptionContent;

import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Provider
public class ValidationMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException e) {
        String[] messages = e.getMessage().split(",");
        for (int i = 0; i < messages.length; i++) {
            messages[i] = messages[i].split(": ")[1];
        }
        ExceptionContent body = new ExceptionContent(
                false,
                Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getReasonPhrase(),
                null,
                messages
        );
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
