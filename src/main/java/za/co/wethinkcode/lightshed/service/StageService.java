package za.co.wethinkcode.lightshed.service;

import za.co.wethinkcode.lightshed.model.Stage;
import java.util.concurrent.atomic.AtomicInteger;

public class StageService {
    private final AtomicInteger currentStage = new AtomicInteger(0);

    public Stage getCurrentStage() {
        return new Stage(currentStage.get());
    }

    public void setStage(int stageValue) {
        Stage stage = new Stage(stageValue);
        currentStage.set(stage.stage());
    }
}
