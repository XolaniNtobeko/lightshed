package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.controller.TownController;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

import static org.junit.jupiter.api.Assertions.*;

public class TownControllerTest {

    private Javalin app;

    @BeforeEach
    void setUp(){
        TownCleaner cleaner = new TownCleaner();
        TownRepository repository = new TownRepository(cleaner);
        repository.loadFromCsv("town.csv");

        TownController controller = new TownController(repository);

        app = Javalin.create(config -> {}).get("/api/towns", controller::getAll);


    }
    @Test
    void testGetAllTownsReturns200andJsonArray(){
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/api/towns");
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("George"));
        }));
    }

    @Test
    void getAllWithSearchQueryFiltersTowns(){
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/api/towns?name=george");
            assertEquals(200, response.code());
            String responseBody = response.body().string();
            assertTrue(responseBody.contains("George"));
            assertFalse(responseBody.contains("Durban"));
        }));
    }
}
