package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.AlertBot;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertBotTest {

    @Test
    public void testAlertBotBroadcastsStageChange() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AlertBot bot = new AlertBot("TestBot");

        // Act
        bot.broadcastStageChange(2);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("TestBot"), "Output should contain the bot name");
        assertTrue(output.contains("Stage 2"), "Output should contain the updated stage number");
    }
}
