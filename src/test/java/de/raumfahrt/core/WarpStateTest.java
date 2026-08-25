package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WarpStateTest {

    @Test
    void initialStateIsInactive() {
        WarpState state = new WarpState();

        assertFalse(state.active());
        assertEquals(0.0, state.remainingSeconds());
        assertEquals(0.0, state.speed());
    }

    @Test
    void activateStartsWarpWithDurationAndSpeed() {
        WarpState state = new WarpState();

        state.activate(15.0, 500.0);

        assertTrue(state.active());
        assertEquals(15.0, state.remainingSeconds());
        assertEquals(500.0, state.speed());
    }

    @Test
    void updateDecrementsRemainingTime() {
        WarpState state = new WarpState();
        state.activate(10.0, 300.0);

        state.update(3.0);

        assertTrue(state.active());
        assertEquals(7.0, state.remainingSeconds());
    }

    @Test
    void warpDeactivatesAfterDurationExpires() {
        WarpState state = new WarpState();
        state.activate(5.0, 300.0);

        state.update(3.0);
        assertTrue(state.active());

        state.update(3.0);
        assertFalse(state.active());
        assertEquals(0.0, state.remainingSeconds());
        assertEquals(0.0, state.speed());
    }

    @Test
    void updateDoesNothingWhenInactive() {
        WarpState state = new WarpState();

        state.update(1.0);

        assertFalse(state.active());
    }
}
