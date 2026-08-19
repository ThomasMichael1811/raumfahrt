package de.raumfahrt.core;

public final class GameLoop implements Runnable {

    private static final long SLEEP_MILLIS = 1L;

    private final long stepNanos;
    private final StepHandler stepHandler;
    private volatile boolean running;
    private Thread thread;

    public GameLoop(int updatesPerSecond, StepHandler stepHandler) {
        this.stepNanos = 1_000_000_000L / updatesPerSecond;
        this.stepHandler = stepHandler;
    }

    public void start() {
        running = true;
        thread = new Thread(this, "game-loop");
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        long previous = System.nanoTime();
        long accumulator = 0L;
        while (running) {
            long now = System.nanoTime();
            accumulator += now - previous;
            previous = now;
            while (accumulator >= stepNanos) {
                stepHandler.step(stepNanos / 1_000_000_000.0);
                accumulator -= stepNanos;
            }
            sleepBriefly();
        }
    }

    private void sleepBriefly() {
        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }
}
