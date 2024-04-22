package com.axonactive.dojo.base.config;

import java.util.ResourceBundle;

public class KafkaConfig {
    private static final ResourceBundle rb = ResourceBundle.getBundle("kafka");
    public static final String ACK = rb.getString("kafka.ack");
    public static final String SERVER = rb.getString("kafka.server");
    public static final String TOPIC = "vamos";
    public static final String TOPIC2 = "vamos2";
}
