package com.axonactive.dojo.base.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConsumerShutdownSignalCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public interface IExchangeChannel {
    public void declareExchange() throws IOException;
    public void declareQueues(String ...queueNames) throws IOException;
    public void performQueueBinding(String queueName, String routingKey) throws IOException;
    public void subscribeMessage(String queueName, DeliverCallback deliverCallback, CancelCallback cancelCallback, ConsumerShutdownSignalCallback consumerShutdownSignalCallback) throws IOException;
    public void publishMessage(String message, String messageKey) throws IOException;
}
