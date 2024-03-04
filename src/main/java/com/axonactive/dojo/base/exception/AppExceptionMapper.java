package com.axonactive.dojo.base.exception;

import com.axonactive.dojo.base.exception.message.LoggingExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {
    private static Logger logger = LogManager.getLogger(AppException.class);
    @Override
    public Response toResponse(AppException e) {
        logger.error(LoggingExceptionMessage.getMessage(e));

        return Response
                .status(e.getContent().getStatusCode())
                .entity(e.getContent())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
