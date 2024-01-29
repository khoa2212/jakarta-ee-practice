package com.axonactive.dojo.base.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {
    @Override
    public Response toResponse(AppException e) {
        return Response
                .status(e.getContent().getStatusCode())
                .entity(e.getContent())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
