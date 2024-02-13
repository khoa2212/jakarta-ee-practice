package com.axonactive.dojo.base.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2ConsoleAndFile {
    private static final Logger logger = LogManager.getLogger(Log4j2ConsoleAndFile.class);

    // Used for testing
    public static void main(String[] args) {
        logger.info("Hello Agile Course!");
    }
}
