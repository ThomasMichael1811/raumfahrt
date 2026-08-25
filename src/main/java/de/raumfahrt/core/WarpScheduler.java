package de.raumfahrt.core;

import java.util.Random;

public final class WarpScheduler {

    private static final double MIN_INTERVAL = 60.0;
    private static final double MAX_INTERVAL = 180.0;
    private static final double MIN_DURATION = 1.0;
    private static final double MAX_DURATION = 4.0;

    private final Random random;
    private final WarpState warpState;
    private final Runnable onWarpEnd;
    private double timeToNextWarp;
    private boolean warpWasActive;

    public WarpScheduler(Random random, WarpState warpState, Runnable onWarpEnd) {
        this.random = random;
        this.warpState = warpState;
        this.onWarpEnd = onWarpEnd;
        this.timeToNextWarp = randomInterval();
        this.warpWasActive = false;
    }

    public void update(double deltaSeconds) {
        boolean active = warpState.active();
        if (active) {
            warpWasActive = true;
            return;
        }
        if (warpWasActive) {
            warpWasActive = false;
            warpState.deactivate();
            if (onWarpEnd != null) {
                onWarpEnd.run();
            }
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
        warpWasActive = false;
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
