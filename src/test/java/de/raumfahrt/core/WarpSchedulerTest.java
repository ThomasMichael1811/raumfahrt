package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class WarpSchedulerTest {

    @Test
    void schedulerStartsInactive() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        assertFalse(state.active());
    }

    @Test
    void triggerNowActivatesWarp() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        scheduler.triggerNow();

        assertTrue(state.active());
        assertTrue(state.remainingSeconds() >= 1.0 && state.remainingSeconds() <= 4.0);
    }

    @Test
    void schedulerTriggersAfterInterval() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        scheduler.update(180.0);

        assertTrue(state.active());
    }

    @Test
    void schedulerDoesNotTriggerBeforeInterval() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        scheduler.update(30.0);

        assertFalse(state.active());
    }

    @Test
    void schedulerResetsAfterWarpEnds() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        scheduler.triggerNow();
        assertTrue(state.active());

        state.update(25.0);
        assertFalse(state.active());

        scheduler.update(200.0);
        assertTrue(state.active());
    }

    @Test
    void schedulerDoesNotTriggerWhileWarpActive() {
        WarpState state = new WarpState();
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> {});

        scheduler.triggerNow();
        double remaining = state.remainingSeconds();

        scheduler.update(10.0);

        assertTrue(state.active());
        assertEquals(remaining, state.remainingSeconds());
    }

    @Test
    void onWarpEndCallbackIsCalled() {
        WarpState state = new WarpState();
        boolean[] callbackCalled = {false};
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), state, () -> callbackCalled[0] = true);

        scheduler.triggerNow();
        scheduler.update(1.0);
        state.update(25.0);
        scheduler.update(1.0);

        assertTrue(callbackCalled[0]);
    }
}
