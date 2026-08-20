package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DemoSimulationTest {

    private static final int W = 600;
    private static final int H = 400;

    @Test
    void erwarteteLueckenzeitIstLueckeDurchGeschwindigkeit() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);
        DemoSimulation simulation = new DemoSimulation(projection, 300.0, 50.0);

        double expected = (projection.rightEntryWorldX(300.0) - projection.leftExitWorldX(300.0)) / 50.0;

        assertEquals(expected, simulation.expectedGapTime(), 1e-9);
    }

    @Test
    void gemesseneLueckenzeitEntsprichtErwarteter() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);
        DemoSimulation simulation = new DemoSimulation(projection, 300.0, 50.0);
        double expected = simulation.expectedGapTime();

        double dt = 0.01;
        int steps = (int) Math.ceil(4.0 / dt);
        for (int i = 0; i < steps; i++) {
            simulation.advance(dt);
        }

        assertEquals(expected, simulation.measuredGapTime(), dt * 2.0);
    }

    @Test
    void simulationSetztNachDurchlaufZurueck() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);
        DemoSimulation simulation = new DemoSimulation(projection, 300.0, 50.0);

        double dt = 0.01;
        int steps = (int) Math.ceil(12.0 / dt);
        for (int i = 0; i < steps; i++) {
            simulation.advance(dt);
        }

        assertTrue(simulation.worldX() >= simulation.startX());
        assertTrue(simulation.worldX() < projection.rightEntryWorldX(300.0) + 1.0);
    }

    @Test
    void geschwindigkeitNullBewegtNichts() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);
        DemoSimulation simulation = new DemoSimulation(projection, 300.0, 0.0);

        simulation.advance(5.0);

        assertEquals(simulation.startX(), simulation.worldX());
    }

    @Test
    void setSpeedWirdWirksam() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);
        DemoSimulation simulation = new DemoSimulation(projection, 300.0, 50.0);

        simulation.setSpeed(100.0);
        simulation.advance(0.5);

        assertEquals(100.0, simulation.speed());
        assertTrue(simulation.worldX() > simulation.startX());
    }
}
