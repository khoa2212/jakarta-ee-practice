package com.axonactive.dojo.base.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingExceptionMessage {
    public static String getMessage(Exception e) {
        StackTraceElement[] stackTraceArray = e.getStackTrace();

        String logMessage = String.format("%s:%s:%d - %s",
                stackTraceArray[0].getClassName(),
                stackTraceArray[0].getMethodName(),
                stackTraceArray[0].getLineNumber(),
                e.getMessage());

        return logMessage;
    }
}
