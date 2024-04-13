package com.axonactive.dojo.base.producer;


import com.rabbitmq.client.*;

import java.io.IOException;

public class TopicExchangeChannel implements IExchangeChannel {
    private Channel channel;
    private String exchangeName;

    public TopicExchangeChannel(Channel channel, String exchangeName) {
        this.channel = channel;
        this.exchangeName = exchangeName;
    }
    public void declareExchange() throws IOException {
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
    }

    public void declareQueues(String ...queueNames) throws IOException {
        for (String queueName : queueNames) {
            channel.queueDeclare(queueName, true, false, false, null);
        }
    }
    public void performQueueBinding(String queueName, String routingKey) throws IOException {
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    public void subscribeMessage(String queueName, DeliverCallback deliverCallback, CancelCallback cancelCallback, ConsumerShutdownSignalCallback consumerShutdownSignalCallback) throws IOException {
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback, consumerShutdownSignalCallback);
    }

    public void publishMessage(String message, String messageKey) throws IOException {
        channel.basicPublish(exchangeName, messageKey, null, message.getBytes());
    }
}
