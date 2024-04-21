package com.axonactive.dojo.base.producer;

import com.axonactive.dojo.base.config.KafkaConfig;
import com.axonactive.dojo.base.kafka_serializer.relative.RelativeDeserializer;
import com.axonactive.dojo.base.kafka_serializer.relative.RelativeSerializer;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@NoArgsConstructor
public class KafkaMessageBroker {
    public static Properties getProducerProperties() {
        Properties propertiesProducer = new Properties();
        propertiesProducer.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.SERVER);
        propertiesProducer.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, RelativeSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACK);

        return propertiesProducer;
    }

    public static void send(ProducerRecord record, Producer producer) {
        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                System.out.println("Send message \n" +
                        "Topic: " + recordMetadata.topic() + "\n" +
                        "Partition: " + recordMetadata.partition() + "\n" +
                        "Offset: " + recordMetadata.offset() + "\n" +
                        "Timestamp: " + recordMetadata.timestamp());
            } else {
                System.out.println(e.toString());
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
