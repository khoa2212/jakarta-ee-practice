package com.axonactive.dojo.base.config;

import java.util.ResourceBundle;

public class RabbitMQConfig {
    private static final ResourceBundle rb = ResourceBundle.getBundle("rabbitmq");
    public static final String HOST = rb.getString("rabbitmq.host");
    public static final String PORT = rb.getString("rabbitmq.port");

    public static final String USER = rb.getString("rabbitmq.user");
    public static final String PASSWORD = rb.getString("rabbitmq.password");
    public static final String VIRTUAL_HOST = rb.getString("rabbitmq.virtual-host");
}
