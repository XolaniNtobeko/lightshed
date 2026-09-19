package za.co.wethinkcode.lightshed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import za.co.wethinkcode.lightshed.controller.TownController;
import za.co.wethinkcode.lightshed.model.Stage;
import za.co.wethinkcode.lightshed.service.ScheduleService;
import za.co.wethinkcode.lightshed.service.StageService;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

public class Main {

    public static Javalin createApp(StageService stageService, ScheduleService scheduleService) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper));
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        });

        TownCleaner townCleaner = new TownCleaner();
        TownRepository townRepository = new TownRepository(townCleaner);
        TownController townController = new TownController(townRepository);

        // Towns endpoint
        app.get("/api/towns", townController::getAll);

        // Stage endpoints
        app.get("/api/stage", ctx -> ctx.json(stageService.getCurrentStage()));
        app.post("/api/stage", ctx -> {
            Stage newStage = ctx.bodyAsClass(Stage.class);
            stageService.setStage(newStage.stage());
            ctx.status(200).json(stageService.getCurrentStage());
        });

        // Schedule endpoint
        app.get("/api/schedule/{province}/{town}", ctx -> {
            String province = ctx.pathParam("province");
            String town = ctx.pathParam("town");
            ctx.json(scheduleService.getScheduleForTown(province, town));
        });

        return app;
    }

    public static void main(String[] args) {
        StageService stageService = new StageService();
        // Schedule service will communicate over network to port 7000
        ScheduleService scheduleService = new ScheduleService("http://localhost:7000");

        Javalin app = createApp(stageService, scheduleService);
        app.start(7000);
        System.out.println("🚀 Server running at http://localhost:7000");
    }
}