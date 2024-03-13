package com.axonactive.dojo.base.config;

import java.util.ResourceBundle;

public class MailConfig {
    private static final ResourceBundle rb = ResourceBundle.getBundle("email");

    public static final String HOST_NAME = rb.getString("email.host.name");

    public static final int SSL_PORT = Integer.parseInt(rb.getString("email.ssl.port"));

    public static final int TSL_PORT = Integer.parseInt(rb.getString("email.tls.port"));

    public static final String APP_EMAIL = rb.getString("email.app.email");

    public static final String APP_PASSWORD = rb.getString("email.app.pass");

    public static final String VERIFY_URL = rb.getString("email.verify.url");
}
