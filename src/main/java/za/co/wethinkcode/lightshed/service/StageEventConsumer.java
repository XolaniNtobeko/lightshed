package za.co.wethinkcode.lightshed.service;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class StageEventConsumer implements AutoCloseable {
    private static final String EXCHANGE_NAME = "lightshed_stage_exchange";
    private static final String QUEUE_NAME = "lightshed_panic_queue";
    private final Connection connection;
    private final Channel channel;

    public StageEventConsumer(String host) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();

            // Declare exchange and bound queue for panic alerts
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RabbitMQ connection for consumer", e);
        }
    }

    public void startConsuming() {
        try {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received stage event: " + message);
                handlePanicCheck(message);
            };
            CancelCallback cancelCallback = consumerTag -> {};
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            System.err.println(" [!] Failed to start consuming messages: " + e.getMessage());
        }
    }

    private void handlePanicCheck(String message) {
        // Trigger a panic warning if stage reaches severe levels (Stage 4 or higher)
        if (message.contains("\"stage\": 4") || message.contains("\"stage\": 5") || message.contains("\"stage\": 6")) {
            System.err.println(" [!] PANIC ALERT: High loadshedding stage detected! Take necessary precautions.");
        }
    }

    @Override
    public void close() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (Exception ignored) {}
    }
}