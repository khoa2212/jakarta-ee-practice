package com.axonactive.dojo.base.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConsumerShutdownSignalCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IExchangeChannel {
    public void declareExchange() throws IOException;
    public void closeExchange() throws IOException, TimeoutException;
    public void declareQueues(String ...queueNames) throws IOException;
    public void performQueueBinding(String queueName, String routingKey) throws IOException;
    public void subscribeMessage(String queueName, DeliverCallback deliverCallback, CancelCallback cancelCallback, ConsumerShutdownSignalCallback consumerShutdownSignalCallback) throws IOException;
    public void publishMessage(String message, String messageKey) throws IOException;
}
