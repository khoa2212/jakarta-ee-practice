package com.axonactive.dojo.base.config;

import java.util.ResourceBundle;

public class KafkaConfig {
    private static final ResourceBundle rb = ResourceBundle.getBundle("kafka");
    public final static String ACK = rb.getString("kafka.ack");
    public final static String SERVER = rb.getString("kafka.server");
    public final static String TOPIC = "vamos";
    public final static String GROUP_ID = "vamos";
    public final static String AUTO_OFFSET_RESET_CONFIG = "earliest"; // Consume messages from the beginning of the topic
}
