package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import za.co.wethinkcode.lightshed.controller.TownController;
import za.co.wethinkcode.lightshed.model.Stage;
import za.co.wethinkcode.lightshed.service.StageService;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        TownCleaner cleaner = new TownCleaner();
        TownRepository repository = new TownRepository(cleaner);
        repository.loadFromCsv("town.csv");

        TownController controller = new TownController(repository);
        StageService stageService = new StageService();

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(7000);

        // Iteration 1 routes
        app.get("/api/towns", controller::getAll);

        // Iteration 2 (Chunk 1): Stage Service routes
        app.get("/api/stage", ctx -> ctx.json(stageService.getCurrentStage()));

        app.post("/api/stage", ctx -> {
            Stage stageReq = ctx.bodyAsClass(Stage.class);
            stageService.setStage(stageReq.stage());
            ctx.status(200).json(stageService.getCurrentStage());
        });

        // Exception mapping for invalid stage payloads (HTTP 400)
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        });

        System.out.println("\n🚀 Server running at http://localhost:7000\n");
    }
}