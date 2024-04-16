package com.axonactive.dojo.base.messagebroker;

import com.axonactive.dojo.relative.entity.Relative;
import com.axonactive.dojo.relative.serializer.RelativeDeserializer;
import com.axonactive.dojo.relative.serializer.RelativeSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.Properties;

@Singleton
public class KafkaMessageBroker {
    private static KafkaProducer<String, Relative> producer;
    private static KafkaConsumer<String, Relative> consumer;

    @PostConstruct
    public void init() {
        Properties propertiesProducer = new Properties();
        propertiesProducer.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageBrokerConfig.SERVER);
        propertiesProducer.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, RelativeSerializer.class.getName());
        propertiesProducer.setProperty(ProducerConfig.ACKS_CONFIG, MessageBrokerConfig.ACKS);

        Properties propertiesConsumer = new Properties();
        propertiesConsumer.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageBrokerConfig.SERVER);
        propertiesConsumer.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propertiesConsumer.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RelativeDeserializer.class.getName());
        propertiesConsumer.setProperty(ConsumerConfig.GROUP_ID_CONFIG, MessageBrokerConfig.GROUP_ID);
        propertiesConsumer.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, MessageBrokerConfig.AUTO_OFFSET_RESET_CONFIG);

        producer = new KafkaProducer<>(propertiesProducer);
        consumer = new KafkaConsumer<>(propertiesConsumer);
    }

    public KafkaProducer<String, Relative> getProducer() {
        return producer;
    }

    public KafkaConsumer<String, Relative> getConsumer() {
        return consumer;
    }

    public void sendMessage(ProducerRecord<String, Relative> record) {
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e==null) {
                    System.out.println("Receive new metadata \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    System.out.println(e.toString());
                }
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
