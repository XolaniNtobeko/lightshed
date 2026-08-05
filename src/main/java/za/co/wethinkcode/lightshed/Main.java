package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import za.co.wethinkcode.lightshed.controller.TownController;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

public class Main {

    public static void main(String[] args) {
        TownCleaner cleaner = new TownCleaner();
        TownRepository repository = new TownRepository(cleaner);
        repository.loadFromCsv("town.csv");

        TownController controller = new TownController(repository);

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(7000);

        app.get("/api/towns", controller::getAll);

        System.out.println("\n🚀 Server running at http://localhost:7000/api/towns\n");
    }
}