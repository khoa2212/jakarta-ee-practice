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

    private final String FIRST_QUEUE_NAME = "DVamos";
    private final String SECOND_QUEUE_NAME = "Vamos 2";
    private final String FANOUT_EXCHANGE_NAME = "fanout-exchange-name";
    private final String DIRECT_EXCHANGE_NAME = "direct-exchange-name";
    private final String TOPIC_EXCHANGE_NAME = "topic-exchange-name";

    private Connection connection;

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

    public void sendMessage(String message) throws  IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(FIRST_QUEUE_NAME, true, false, false, null);

        System.out.println("Start sending message");
        channel.basicPublish("", FIRST_QUEUE_NAME, null, message.getBytes()); // Default exchange
    }

    public void sendMessageFanoutExchange(String message) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);
        channel.queueDeclare(FIRST_QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(SECOND_QUEUE_NAME, true, false, false, null);

        channel.queueBind(FIRST_QUEUE_NAME, FANOUT_EXCHANGE_NAME, "");
        channel.queueBind(SECOND_QUEUE_NAME, FANOUT_EXCHANGE_NAME, "");

        System.out.println("Start sending message with fanout exchange");
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, message.getBytes());
    }

    public void sendMessageDirectExchange(String message, String routingKey) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);

        channel.queueDeclare(FIRST_QUEUE_NAME, true, false, false, null);

        channel.queueBind(FIRST_QUEUE_NAME, DIRECT_EXCHANGE_NAME, routingKey);

        System.out.println("Start sending message with direct exchange");
        channel.basicPublish(DIRECT_EXCHANGE_NAME, routingKey, null, message.getBytes());
    }
}
