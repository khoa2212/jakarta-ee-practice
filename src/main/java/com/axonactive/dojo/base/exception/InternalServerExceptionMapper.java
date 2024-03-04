package com.axonactive.dojo.base.exception;

import com.axonactive.dojo.base.exception.content.ExceptionContent;
import com.axonactive.dojo.base.exception.message.LoggingExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InternalServerExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger logger = LogManager.getLogger(RuntimeException.class);
    @Override
    public Response toResponse(RuntimeException e) {
        logger.error(LoggingExceptionMessage.getMessage(e));

        ExceptionContent content = new ExceptionContent(
                false,
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                null
        );
        return Response
                .status(content.getStatusCode())
                .entity(content)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
