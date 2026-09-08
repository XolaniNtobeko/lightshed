package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.StageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StageServiceTest {

    private StageService stageService;

    @BeforeEach
    void setUp() {
        stageService = new StageService();
    }

    @Test
    void defaultStageShouldBeZero() {
        assertEquals(0, stageService.getCurrentStage().stage());
    }

    @Test
    void shouldUpdateStageSuccessfully() {
        stageService.setStage(4);
        assertEquals(4, stageService.getCurrentStage().stage());
    }

    @Test
    void shouldRejectInvalidStages() {
        assertThrows(IllegalArgumentException.class, () -> stageService.setStage(-1));
        assertThrows(IllegalArgumentException.class, () -> stageService.setStage(9));
    }
}