package com.axonactive.dojo.base.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(1)
public class LoggerFilter implements ContainerRequestFilter {
    private static final Logger logger = LogManager.getLogger(LoggerFilter.class);

    @Context
    HttpServletRequest httpRequest;

    @Override
    public void filter(ContainerRequestContext reqCtx) throws IOException {
        String ip = httpRequest.getRemoteAddr();
        String path = reqCtx.getUriInfo().getAbsolutePath().getPath();
        String method = reqCtx.getMethod();
        String message = String.format("%s:%s:%s", ip, method, path);

        logger.info(message);
    }
}
