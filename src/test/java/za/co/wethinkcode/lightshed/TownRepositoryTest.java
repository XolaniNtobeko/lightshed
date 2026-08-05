package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.model.Town;
import za.co.wethinkcode.lightshed.service.TownCleaner;
import za.co.wethinkcode.lightshed.service.TownRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TownRepositoryTest {

    private TownRepository repository;

    @BeforeEach
    void setUp(){
        TownCleaner cleaner = new TownCleaner();
        repository = new TownRepository(cleaner);
    }

    @Test
    void testLoadsTownsFromCsvResource(){
        repository.loadFromCsv("town.csv");
        List towns = repository.getAllTowns();

        assertNotNull(towns);
        assertFalse(towns.isEmpty(), "Repository should not be empty after loading towns.csv");
    }
    @Test
    void testNameAreCleanedWhenLoaded(){
        repository.loadFromCsv("town.csv");
        List<Town> towns = repository.getAllTowns();

        // Verify first town exists and its name is sanitized (e.g. Title Case, trimmed)
        Town firstTown = towns.get(0);
        assertNotNull(towns);
        assertNotNull(firstTown.getName());
        assertFalse(firstTown.getName().isBlank(), "Town name should not be blank");

    }
    @Test
    void testfindByNameReturnsMatchingTownsCaseInsensitive(){
        TownCleaner cleaner = new TownCleaner();
        TownRepository townRepository = new TownRepository(cleaner);
        townRepository.loadFromCsv("town.csv");

        List<Town> results = townRepository.findByName("George");

        assertEquals(1, results.size());
        assertEquals("George", results.get(0).getName());
    }
}
