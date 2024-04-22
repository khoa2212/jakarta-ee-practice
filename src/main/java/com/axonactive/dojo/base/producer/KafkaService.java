package com.axonactive.dojo.base.producer;

import com.axonactive.dojo.base.config.KafkaConfig;
import com.axonactive.dojo.base.kafka_serializer.BaseSerializer;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@NoArgsConstructor
public class KafkaService {
    public static Properties getProducerProperties() {
        Properties propertiesProducer = new Properties();
        propertiesProducer.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.SERVER);
        propertiesProducer.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BaseSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACK);

        return propertiesProducer;
    }

    public static void send(ProducerRecord record, Producer producer) {
        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                System.out.println("Send message");
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
