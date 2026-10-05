package de.raumfahrt.core;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class WarpTransitionController {

    public static final double DURATION_SECONDS = 2.0;
    public static final double SCENE_SWITCH_PROGRESS = 0.5;
    private static final double WARP_SPEED = 900.0;

    private final WarpState warpState;
    private final Consumer<SceneType> sceneSwitcher;
    private final Supplier<SceneType> currentScene;
    private final BooleanSupplier paused;
    private boolean active;
    private boolean independent;
    private boolean sceneSwitched;
    private SceneType targetScene;
    private double elapsedSeconds;

    public WarpTransitionController(
            WarpState warpState, Consumer<SceneType> sceneSwitcher, Supplier<SceneType> currentScene) {
        this(warpState, sceneSwitcher, currentScene, () -> false);
    }

    public WarpTransitionController(
            WarpState warpState,
            Consumer<SceneType> sceneSwitcher,
            Supplier<SceneType> currentScene,
            BooleanSupplier paused) {
        this.warpState = warpState;
        this.sceneSwitcher = sceneSwitcher;
        this.currentScene = currentScene;
        this.paused = paused;
    }

    public boolean startSceneTransition(SceneType target) {
        if (active || target == null || target == currentScene.get()) {
            return false;
        }
        begin(target, false);
        return true;
    }

    public boolean startIndependentWarp() {
        if (active) {
            return false;
        }
        begin(null, true);
        return true;
    }

    public void update(double deltaSeconds) {
        if (!active || paused.getAsBoolean()) {
            return;
        }
        elapsedSeconds += deltaSeconds;
        warpState.update(deltaSeconds);
        switchSceneAtMidpoint();
        if (elapsedSeconds >= DURATION_SECONDS || !warpState.active()) {
            active = false;
        }
    }

    public boolean active() {
        return active;
    }

    public boolean independent() {
        return independent;
    }

    public SceneType targetScene() {
        return targetScene;
    }

    public double progress() {
        return Math.max(0.0, Math.min(1.0, elapsedSeconds / DURATION_SECONDS));
    }

    private void begin(SceneType target, boolean independentWarp) {
        this.active = true;
        this.independent = independentWarp;
        this.sceneSwitched = false;
        this.targetScene = target;
        this.elapsedSeconds = 0.0;
        warpState.activate(DURATION_SECONDS, WARP_SPEED);
    }

    private void switchSceneAtMidpoint() {
        if (independent || sceneSwitched || progress() < SCENE_SWITCH_PROGRESS) {
            return;
        }
        sceneSwitched = true;
        sceneSwitcher.accept(targetScene);
    }
}
