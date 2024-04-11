package com.axonactive.dojo.base.producer;

import com.axonactive.dojo.base.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Singleton
@Startup
public class RabbitMQProducer {

    private ConnectionFactory factory;

    @PostConstruct
    private void init() {
        factory = new ConnectionFactory();
        factory.setVirtualHost(RabbitMQConfig.VIRTUAL_HOST);
        factory.setHost(RabbitMQConfig.HOST);
        factory.setPort(Integer.parseInt(RabbitMQConfig.PORT));
        factory.setUsername(RabbitMQConfig.USER);
        factory.setPassword(RabbitMQConfig.PASSWORD);
    }

    public void sendMessage(String message) throws  IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitMQConfig.QUEUE_NAME, false, false, false, null);

        System.out.println("Start sending message");
        channel.basicPublish("", RabbitMQConfig.QUEUE_NAME, null, message.getBytes()); // Default exchange
    }
}
