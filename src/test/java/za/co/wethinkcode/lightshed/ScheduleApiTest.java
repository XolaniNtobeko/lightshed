package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.ScheduleService;
import za.co.wethinkcode.lightshed.service.StageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleApiTest {

    @Test
    void getScheduleShouldReturn200AndActiveSlots() {
        StageService stageService = new StageService();
        stageService.setStage(2);
        ScheduleService scheduleService = new ScheduleService(stageService);

        Javalin app = Main.createApp(stageService, scheduleService);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/api/schedule/Western Cape/George");

            assertEquals(200, response.code());
            String responseBody = response.body().string();
            assertTrue(responseBody.contains("George"));
            assertTrue(responseBody.contains("Western Cape"));
            assertTrue(responseBody.contains("currentStage\":2"));
        });
    }
}