package com.axonactive.dojo.base.config;

import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.ws.rs.Produces;
import java.util.logging.Logger;

public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
