package za.co.wethinkcode.lightshed.model;

import java.util.List;

public record TownSchedule(
        String town,
        String province,
        int currentStage,
        List<ScheduleSlot> activeSlots
) {}