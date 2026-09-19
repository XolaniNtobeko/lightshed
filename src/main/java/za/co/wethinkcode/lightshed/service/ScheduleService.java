package za.co.wethinkcode.lightshed.service;

import za.co.wethinkcode.lightshed.model.ScheduleSlot;
import za.co.wethinkcode.lightshed.model.TownSchedule;

import java.time.LocalTime;
import java.util.List;

public class ScheduleService {

    private static final List<ScheduleSlot> MASTER_SLOTS = List.of(
            new ScheduleSlot(LocalTime.of(0, 0), LocalTime.of(2, 30), 1),
            new ScheduleSlot(LocalTime.of(8, 0), LocalTime.of(10, 30), 2),
            new ScheduleSlot(LocalTime.of(16, 0), LocalTime.of(18, 30), 3),
            new ScheduleSlot(LocalTime.of(20, 0), LocalTime.of(22, 30), 4)
    );

    private final StageService stageService;

    public ScheduleService(StageService stageService) {
        this.stageService = stageService;
    }

    public TownSchedule getScheduleForTown(String province, String town) {
        int currentStage = stageService.getCurrentStage().stage();

        List<ScheduleSlot> activeSlots = MASTER_SLOTS.stream()
                .filter(slot -> slot.stageThreshold() <= currentStage)
                .toList();

        return new TownSchedule(town, province, currentStage, activeSlots);
    }
}