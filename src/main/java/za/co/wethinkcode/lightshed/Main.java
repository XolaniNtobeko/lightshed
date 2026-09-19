package za.co.wethinkcode.lightshed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import za.co.wethinkcode.lightshed.controller.TownController;
import za.co.wethinkcode.lightshed.model.Stage;
import za.co.wethinkcode.lightshed.service.ScheduleService;
import za.co.wethinkcode.lightshed.service.StageEventConsumer;
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

        // Initialize and start the background RabbitMQ event consumer for panic alerts
        StageEventConsumer eventConsumer = null;
        try {
            String rabbitHost = System.getenv("RABBITMQ_HOST");
            if (rabbitHost == null || rabbitHost.isEmpty()) {
                rabbitHost = "localhost";
            }
            eventConsumer = new StageEventConsumer(rabbitHost);
            eventConsumer.startConsuming();
            System.out.println(" [x] StageEventConsumer successfully started and listening for events.");
        } catch (Exception e) {
            System.err.println(" [!] Warning: Could not start StageEventConsumer (Broker offline?): " + e.getMessage());
        }

        Javalin app = createApp(stageService, scheduleService);
        app.start(7000);
        System.out.println("🚀 Server running at http://localhost:7000");

        // Ensure clean resource closure if the application shuts down
        final StageEventConsumer consumerToClose = eventConsumer;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (consumerToClose != null) {
                consumerToClose.close();
                System.out.println(" [x] StageEventConsumer connection closed cleanly.");
            }
        }));
    }
}