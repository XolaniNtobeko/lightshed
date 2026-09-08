package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.StageService;

public class StageServiceTest {
    private StageService stageService;

    @BeforeEach
    void setUp(){
        stageService = new StageService();
    }
    @Test
    void defaultStageShouldBeZero(){
        assertThat(stageService.getCurrentStage().stage()).isEqualTo(0);
    }
}
