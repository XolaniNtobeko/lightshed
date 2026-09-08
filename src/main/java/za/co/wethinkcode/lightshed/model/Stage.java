package za.co.wethinkcode.lightshed.model;

public record Stage(int stage) {
    public Stage {
        if (stage < 0 || stage > 8) {
            throw new IllegalArgumentException("Stage must be between 0 and 8");
        }
    }
}