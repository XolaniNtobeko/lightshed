package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.StageEventConsumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StageEventConsumerTest {

    @Test
    void constructorShouldThrowRuntimeExceptionWhenBrokerUnreachable() {
        assertThrows(RuntimeException.class, () -> {
            new StageEventConsumer("invalid-nonexistent-host-999");
        });
    }
}