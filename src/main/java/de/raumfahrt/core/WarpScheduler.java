package de.raumfahrt.core;

import java.util.Random;

public final class WarpScheduler {

    private static final double MIN_INTERVAL = 60.0;
    private static final double MAX_INTERVAL = 180.0;
    private static final double MIN_DURATION = 10.0;
    private static final double MAX_DURATION = 20.0;

    private final Random random;
    private final WarpState warpState;
    private double timeToNextWarp;

    public WarpScheduler(Random random, WarpState warpState) {
        this.random = random;
        this.warpState = warpState;
        this.timeToNextWarp = randomInterval();
    }

    public void update(double deltaSeconds) {
        if (warpState.active()) {
            return;
        }
        timeToNextWarp -= deltaSeconds;
        if (timeToNextWarp <= 0.0) {
            warpState.activate(randomDuration(), warpSpeed());
            timeToNextWarp = randomInterval();
        }
    }

    public void triggerNow() {
        warpState.activate(randomDuration(), warpSpeed());
        timeToNextWarp = randomInterval();
    }

    private double randomInterval() {
        return MIN_INTERVAL + random.nextDouble() * (MAX_INTERVAL - MIN_INTERVAL);
    }

    private double randomDuration() {
        return MIN_DURATION + random.nextDouble() * (MAX_DURATION - MIN_DURATION);
    }

    private double warpSpeed() {
        return 500.0 + random.nextDouble() * 500.0;
    }
}
