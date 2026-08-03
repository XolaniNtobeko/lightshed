package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.TownCleaner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TownCleanerTest {
    private TownCleaner townCleaner;

    @BeforeEach
    void setUp(){
        townCleaner = new TownCleaner();
    }
    @Test
    void testTrimsSpaceAndFormatsCapitalization(){
        assertEquals("George", townCleaner.cleanText(" george "));
    }
    @Test
    void testHandlesMultiWordTown(){
        assertEquals("Cape Town", townCleaner.cleanText("   CAPE  TOWN  "));
    }
    @Test
    void testHandlesMixedCaseInput(){
        assertEquals("Johannesburg", townCleaner.cleanText("jOhAnNeSbUrG"));
    }
    @Test
    void testHandlesNullBlankInput(){
        assertEquals("", townCleaner.cleanText(null));
        assertEquals("", townCleaner.cleanText(" "));
    }
}
