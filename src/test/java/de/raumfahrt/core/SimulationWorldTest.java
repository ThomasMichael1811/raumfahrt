package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SimulationWorldTest {

    private static final int WIDTH = 1280;

    private static SimulationWorld createWorld() {
        StarField starField = new StarField(WIDTH, List.of(new Star(100, 50, 1.0, 1.0, 1)));
        MeteorField meteorField = new MeteorField(WIDTH, 0, new MeteorSpawner(new java.util.Random(1), WIDTH, 100));
        Sun sun = new Sun(500, 50, 30, 5.0);
        return new SimulationWorld(WIDTH, starField, meteorField, sun);
    }

    @Test
    void updateBewegtWeltObjektive() {
        SimulationWorld world = createWorld();

        world.update(1.0);

        assertEquals(55, world.stars().get(0).x(), 1e-9);
        assertEquals(495, world.sun().x(), 1e-9);
    }

    @Test
    void pausierteWeltBleibtUnveraendert() {
        SimulationWorld world = createWorld();

        world.togglePause();
        world.update(1.0);

        assertEquals(100, world.stars().get(0).x(), 1e-9);
        assertEquals(500, world.sun().x(), 1e-9);
    }

    @Test
    void togglePauseSchaltetZustandUm() {
        SimulationWorld world = createWorld();

        world.togglePause();

        assertTrue(world.isPaused());
        world.togglePause();
        assertFalse(world.isPaused());
    }

    @Test
    void moveCameraVerschiebtKamera() {
        SimulationWorld world = createWorld();

        world.moveCamera(1.0, 1.0);

        assertEquals(120.0, world.cameraX(), 1e-9);
    }

    @Test
    void moveCameraKlemmtAnGrenzen() {
        SimulationWorld world = createWorld();

        world.moveCamera(-10.0, 1.0);

        assertEquals(0.0, world.cameraX(), 1e-9);
        world.moveCamera(10.0, 10.0);
        assertEquals(WIDTH, world.cameraX(), 1e-9);
    }

    @Test
    void pausierteWeltIgnoriertKameraInput() {
        SimulationWorld world = createWorld();

        world.togglePause();
        world.moveCamera(1.0, 1.0);

        assertEquals(0.0, world.cameraX(), 1e-9);
    }

    @Test
    void meteorsWerdenGeteilt() {
        SimulationWorld world = createWorld();

        assertTrue(world.meteors().isEmpty());
    }
}
