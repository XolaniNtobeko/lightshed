package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceNameServerTest {
    private Javalin app;
    @BeforeEach
    void setUp(){
        TownCleaner cleaner = new TownCleaner();
        TownRepository repository = new TownRepository(cleaner);
        repository.loadFromCsv("town.csv");

        PlaceNameServer server= new PlaceNameServer(repository);
        app = server.getApp();
    }
    @Test
    void testGetTownByNameReturns200AndTown(){
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/api/towns/George");

            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("George"));
        }));
    }
    @Test
    void testGetTownByNameReturns404WhenNotFound(){
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/api/towns/UnkownTown");
            assertEquals(404, response.code());;
        }));
    }
}
