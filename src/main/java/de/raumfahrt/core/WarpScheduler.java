package de.raumfahrt.core;

import java.util.Random;
import java.util.function.Supplier;

public final class WarpScheduler {

    private static final double MIN_INTERVAL = 60.0;
    private static final double MAX_INTERVAL = 180.0;

    private final Random random;
    private final WarpTransitionController controller;
    private final Supplier<SceneType> nextScene;
    private double timeToNextWarp;
    private boolean transitionWasActive;

    public WarpScheduler(Random random, WarpTransitionController controller, Supplier<SceneType> nextScene) {
        this.random = random;
        this.controller = controller;
        this.nextScene = nextScene;
        this.timeToNextWarp = randomInterval();
        this.transitionWasActive = false;
    }

    public void update(double deltaSeconds) {
        if (controller.active()) {
            transitionWasActive = true;
            return;
        }
        if (transitionWasActive) {
            transitionWasActive = false;
            timeToNextWarp = randomInterval();
        }
        timeToNextWarp -= deltaSeconds;
        if (timeToNextWarp <= 0.0) {
            controller.startSceneTransition(nextScene.get());
            timeToNextWarp = randomInterval();
        }
    }

    public double timeToNextWarp() {
        return timeToNextWarp;
    }

    private double randomInterval() {
        return MIN_INTERVAL + random.nextDouble() * (MAX_INTERVAL - MIN_INTERVAL);
    }
}
