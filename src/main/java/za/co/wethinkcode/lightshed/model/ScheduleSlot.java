package za.co.wethinkcode.lightshed.model;

import java.time.LocalTime;

public record ScheduleSlot(LocalTime startTime, LocalTime endTime, int stageThreshold) {}