package com.axonactive.dojo.base.exception;

import com.axonactive.dojo.base.exception.content.ExceptionContent;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

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
                messages);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
