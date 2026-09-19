package za.co.wethinkcode.lightshed.service;

import za.co.wethinkcode.lightshed.model.Stage;
import java.util.concurrent.atomic.AtomicInteger;

public class StageService {
    private final AtomicInteger currentStage = new AtomicInteger(0);
    private final StageEventPublisher eventPublisher;

    // Default constructor for production/runtime
    public StageService() {
        StageEventPublisher pub = null;
        try {
            String host = System.getenv("RABBITMQ_HOST");
            if (host == null || host.isEmpty()) {
                host = "localhost";
            }
            pub = new StageEventPublisher(host);
        } catch (Exception e) {
            System.err.println("Warning: Running without active RabbitMQ broker connection: " + e.getMessage());
        }
        this.eventPublisher = pub;
    }

    // Overloaded constructor for unit testing with mocks/stubs
    public StageService(StageEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Stage getCurrentStage() {
        return new Stage(currentStage.get());
    }

    public void setStage(int stageValue) {
        Stage stage = new Stage(stageValue);
        currentStage.set(stage.stage());

        if (eventPublisher != null) {
            try {
                eventPublisher.publishStageChange(stage.stage());
            } catch (Exception e) {
                System.err.println("Failed to publish stage event: " + e.getMessage());
            }
        }
    }
}