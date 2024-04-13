package com.axonactive.dojo.base.messagebroker;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.Properties;

@Singleton
public class KafkaMessageBroker {
    private static KafkaProducer<String, String> producer;
    private static KafkaConsumer<String, String> consumer;

    @PostConstruct
    public void init() {
//        Properties propertiesProducer = new Properties();
//        propertiesProducer.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageBrokerConfig.SERVER);
//        propertiesProducer.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        propertiesProducer.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        propertiesProducer.setProperty(ProducerConfig.ACKS_CONFIG, MessageBrokerConfig.ACKS);

        Properties propertiesConsumer = new Properties();
        propertiesConsumer.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageBrokerConfig.SERVER);
        propertiesConsumer.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propertiesConsumer.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propertiesConsumer.setProperty(ConsumerConfig.GROUP_ID_CONFIG, MessageBrokerConfig.GROUP_ID);
        propertiesConsumer.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, MessageBrokerConfig.AUTO_OFFSET_RESET_CONFIG);

//        producer = new KafkaProducer<>(propertiesProducer);
        consumer = new KafkaConsumer<>(propertiesConsumer);
    }

    public KafkaProducer<String, String> getProducer() {
        return producer;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    public void sendMessage(ProducerRecord<String, String> record) {
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
