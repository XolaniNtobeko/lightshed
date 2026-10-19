package za.co.wethinkcode.lightshed.service;

public class AlertBot {
    private final String botName;

    public AlertBot(String botName) {
        this.botName = botName;
    }

    public void broadcastStageChange(int newStage) {
        System.out.println(String.format("[%s] Loadshedding stage updated to Stage %d", botName, newStage));
    }
}