package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class EffectDispatcherTest {

    @Test
    void triggerFuehrtRegistriertenEffektAus() {
        EffectDispatcher dispatcher = new EffectDispatcher();
        AtomicInteger runs = new AtomicInteger();
        dispatcher.register(1, runs::incrementAndGet);

        boolean triggered = dispatcher.trigger(1);

        assertTrue(triggered);
        assertEquals(1, runs.get());
    }

    @Test
    void triggerOhneRegistrierungLiefertFalse() {
        EffectDispatcher dispatcher = new EffectDispatcher();

        assertFalse(dispatcher.trigger(9));
    }

    @Test
    void registerUeberschreibtEffektProTaste() {
        EffectDispatcher dispatcher = new EffectDispatcher();
        AtomicInteger runs = new AtomicInteger();
        dispatcher.register(1, () -> {});
        dispatcher.register(1, runs::incrementAndGet);

        dispatcher.trigger(1);

        assertEquals(1, runs.get());
    }
}
