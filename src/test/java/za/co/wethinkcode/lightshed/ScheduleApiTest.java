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

        JavalinTest.test((app, client) -> {
            // Construct ScheduleService with the dynamic test server URL
            ScheduleService scheduleService = new ScheduleService("http://localhost:" + app.port());

            // Register routes on test app
            app.get("/api/stage", ctx -> ctx.json(stageService.getCurrentStage()));
            app.get("/api/schedule/{province}/{town}", ctx -> {
                String province = ctx.pathParam("province");
                String town = ctx.pathParam("town");
                ctx.json(scheduleService.getScheduleForTown(province, town));
            });

            var response = client.get("/api/schedule/Western Cape/George");

            assertEquals(200, response.code());
            String responseBody = response.body().string();
            assertTrue(responseBody.contains("George"));
            assertTrue(responseBody.contains("Western Cape"));
            assertTrue(responseBody.contains("currentStage\":2"));
        });
    }
}