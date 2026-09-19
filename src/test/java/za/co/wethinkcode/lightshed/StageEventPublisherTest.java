package za.co.wethinkcode.lightshed;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.lightshed.service.StageEventPublisher;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StageEventPublisherTest {

    @Test
    void constructorShouldThrowRuntimeExceptionWhenBrokerUnreachable() {
        // Attempting to connect to an invalid host should trigger the catch block and throw a RuntimeException
        assertThrows(RuntimeException.class, () -> {
            new StageEventPublisher("invalid-nonexistent-host-999");
        });
    }
}