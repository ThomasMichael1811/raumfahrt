package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLoopTest {

    private static final long TIMEOUT_MILLIS = 5_000L;

    @Test
    void startFuehrtSchritteMitFesterRateAus() throws Exception {
        AtomicInteger steps = new AtomicInteger();
        GameLoop loop = new GameLoop(1_000, deltaSeconds -> steps.incrementAndGet());

        loop.start();
        await(() -> steps.get() >= 5);
        loop.stop();

        assertTrue(steps.get() >= 5);
    }

    @Test
    void schrittErhaeltKonstantenZeitschritt() throws Exception {
        List<Double> deltas = new CopyOnWriteArrayList<>();
        GameLoop loop = new GameLoop(100, deltas::add);

        loop.start();
        await(() -> deltas.size() >= 3);
        loop.stop();

        assertEquals(0.01, deltas.get(0), 1e-9);
    }

    @Test
    void stopBeendetThreadOhneLeak() throws Exception {
        GameLoop loop = new GameLoop(1_000, deltaSeconds -> {
        });
        loop.start();
        await(loop::isRunning);

        loop.stop();
        await(() -> !gameLoopThreadAlive());

        assertFalse(gameLoopThreadAlive());
    }

    private boolean gameLoopThreadAlive() {
        return Thread.getAllStackTraces().keySet().stream()
            .anyMatch(t -> t.getName().equals("game-loop") && t.isAlive());
    }

    private void await(java.util.function.BooleanSupplier condition) throws InterruptedException {
        long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
        while (!condition.getAsBoolean()) {
            if (System.currentTimeMillis() > deadline) {
                throw new AssertionError("Bedingung nicht innerhalb von " + TIMEOUT_MILLIS + " ms erfüllt");
            }
            Thread.sleep(5);
        }
    }
}
