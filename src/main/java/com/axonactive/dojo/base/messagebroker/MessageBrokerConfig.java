package com.axonactive.dojo.base.messagebroker;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MessageBrokerConfig {
    public final static String SERVER = "127.0.0.1:9092";
    public final static String ACKS = "1";
    public final static String GROUP_ID = "agile-course";
    public final static String TOPIC = "agile-course";
    public final static String AUTO_OFFSET_RESET_CONFIG = "earliest"; // Consume messages from the beginning of the topic
}
