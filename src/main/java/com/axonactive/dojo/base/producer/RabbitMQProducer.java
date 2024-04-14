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

    private static final String QFANOUT1 = "QFANOUT1";
    private static final String QFANOUT2 = "QFANOUT2";
    private static final String FANOUT_EXCHANGE_NAME = "VamosFanoutExchange";
    private static final String QJAVA = "QJAVA";
    private static final String QGENERAL = "QGENERAL";
    private static final String TOPIC_EXCHANGE_NAME = "VamosTopicExchange";
    private static final String JAVA_ROUTING_KEY = "java.*.general.com";
    private static final String GENERAL_ROUTING_KEY = "#.general.com";
    private static final String DIRECT_EXCHANGE_NAME = "VamosDirectExchange";
    private static final String QVAMOS = "QVAMOS";
    private static final String VAMOS_ROUTING_KEY = "Vamos";

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
            case FANOUT -> {
                exchangeChannel = new FanoutExchangeChannel(connection.createChannel(), FANOUT_EXCHANGE_NAME);
                exchangeChannel.declareExchange();
                exchangeChannel.declareQueues(QFANOUT1, QFANOUT2);
                exchangeChannel.performQueueBinding(QFANOUT1, "");
                exchangeChannel.performQueueBinding(QFANOUT2, "");
            }
            case TOPIC -> {
                exchangeChannel = new TopicExchangeChannel(connection.createChannel(), TOPIC_EXCHANGE_NAME);
                exchangeChannel.declareExchange();
                exchangeChannel.declareQueues(QJAVA, QGENERAL);
                exchangeChannel.performQueueBinding(QJAVA, JAVA_ROUTING_KEY);
                exchangeChannel.performQueueBinding(QGENERAL, GENERAL_ROUTING_KEY);
            }
            case DIRECT -> {
                exchangeChannel = new DirectExchangeChannel(connection.createChannel(), DIRECT_EXCHANGE_NAME);
                exchangeChannel.declareExchange();
                exchangeChannel.declareQueues(QVAMOS);
                exchangeChannel.performQueueBinding(QVAMOS, VAMOS_ROUTING_KEY);
            }
        }
    }

    public void send(String message, String messageKey) throws IOException, TimeoutException {
        exchangeChannel.publishMessage(message, messageKey);
    }

    public void closeChannel() throws IOException, TimeoutException {
        exchangeChannel.closeExchange();
    }
}
