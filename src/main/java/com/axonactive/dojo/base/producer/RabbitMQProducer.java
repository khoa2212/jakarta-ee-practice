package com.axonactive.dojo.base.producer;

import com.axonactive.dojo.base.config.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;
@Singleton
@Startup
public class RabbitMQProducer {

    private final String QFANOUT1 = "QFANOUT1";
    private final String QFANOUT2 = "QFANOUT2";
    private final String FANOUT_EXCHANGE_NAME = "VamosFanoutExchange";
    private final String QJAVA = "QJAVA";
    private final String QGENERAL = "QGENERAL";
    private final String TOPIC_EXCHANGE_NAME = "VamosTopicExchange";
    public static final String JAVA_ROUTING_KEY = "java.*.general.com";
    public static final String GENERAL_ROUTING_KEY = "#.general.com";
    private Connection connection;
    private IExchangeChannel exchangeChannel;

    @PostConstruct
    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(RabbitMQConfig.VIRTUAL_HOST);
        factory.setHost(RabbitMQConfig.HOST);
        factory.setPort(Integer.parseInt(RabbitMQConfig.PORT));
        factory.setUsername(RabbitMQConfig.USER);
        factory.setPassword(RabbitMQConfig.PASSWORD);

        connection = factory.newConnection("Producer");
    }

    public void start(BuiltinExchangeType type) throws IOException {
        switch (type) {
            case TOPIC -> {
                exchangeChannel = new TopicExchangeChannel(connection.createChannel(), TOPIC_EXCHANGE_NAME);
                exchangeChannel.declareExchange();
                exchangeChannel.declareQueues(QJAVA, QGENERAL);
                exchangeChannel.performQueueBinding(QJAVA, JAVA_ROUTING_KEY);
                exchangeChannel.performQueueBinding(QGENERAL, GENERAL_ROUTING_KEY);
            }
        }
    }

    public void sendMessageFanoutExchange(String message) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);
        channel.queueDeclare(QFANOUT1, true, false, false, null);
        channel.queueDeclare(QFANOUT2, true, false, false, null);

        channel.queueBind(QFANOUT1, FANOUT_EXCHANGE_NAME, "");
        channel.queueBind(QFANOUT2, FANOUT_EXCHANGE_NAME, "");

        System.out.println("Start sending message");
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, message.getBytes());
    }

    public void send(String message, String messageKey) throws IOException {
        exchangeChannel.publishMessage(message, messageKey);
    }
}
