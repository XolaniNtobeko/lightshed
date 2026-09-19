package za.co.wethinkcode.lightshed.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class StageEventPublisher implements AutoCloseable {
    private static final String EXCHANGE_NAME = "lightshed_stage_exchange";
    private final Connection connection;
    private final Channel channel;

    public StageEventPublisher(String host) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RabbitMQ connection", e);
        }
    }

    public void publishStageChange(int newStage) {
        try {
            String message = String.format("{\"stage\": %d, \"timestamp\": \"%s\"}", newStage, java.time.Instant.now());
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent stage change event: " + message);
        } catch (Exception e) {
            System.err.println(" [!] Failed to publish stage change event: " + e.getMessage());
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