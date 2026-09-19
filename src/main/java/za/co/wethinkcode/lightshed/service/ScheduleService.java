package za.co.wethinkcode.lightshed.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.lightshed.model.ScheduleSlot;
import za.co.wethinkcode.lightshed.model.Stage;
import za.co.wethinkcode.lightshed.model.TownSchedule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.List;

public class ScheduleService {

    private static final List<ScheduleSlot> MASTER_SLOTS = List.of(
            new ScheduleSlot(LocalTime.of(0, 0), LocalTime.of(2, 30), 1),
            new ScheduleSlot(LocalTime.of(8, 0), LocalTime.of(10, 30), 2),
            new ScheduleSlot(LocalTime.of(16, 0), LocalTime.of(18, 30), 3),
            new ScheduleSlot(LocalTime.of(20, 0), LocalTime.of(22, 30), 4)
    );

    private final String stageServiceUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ScheduleService(String stageServiceUrl) {
        this.stageServiceUrl = stageServiceUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public TownSchedule getScheduleForTown(String province, String town) {
        int currentStage = fetchCurrentStageFromService();

        List<ScheduleSlot> activeSlots = MASTER_SLOTS.stream()
                .filter(slot -> slot.stageThreshold() <= currentStage)
                .toList();

        return new TownSchedule(town, province, currentStage, activeSlots);
    }

    private int fetchCurrentStageFromService() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(stageServiceUrl + "/api/stage"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Stage stage = objectMapper.readValue(response.body(), Stage.class);
                return stage.stage();
            } else {
                throw new RuntimeException("Failed to fetch stage. HTTP status: " + response.statusCode());
            }
        } catch (Exception e) {
            // Default to stage 0 on partial failure / network error
            return 0;
        }
    }
}