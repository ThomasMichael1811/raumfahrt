package de.raumfahrt.core;

import java.util.List;

public final class SimulationWorld {

    private static final double CAMERA_SPEED = 120.0;

    private final int width;
    private final int height;
    private final StarField starField;
    private final MeteorField meteorField;
    private final WarpState warpState;
    private Sun sun;
    private double cameraX;
    private boolean paused;
    private SceneType scene;

    public SimulationWorld(int width, StarField starField, MeteorField meteorField, Sun sun) {
        this(width, starField, meteorField, sun, new WarpState());
    }

    public SimulationWorld(int width, StarField starField, MeteorField meteorField, Sun sun, WarpState warpState) {
        this.width = width;
        this.height = sun.y() > 0 ? (int) (sun.y() * 2) : 600;
        this.starField = starField;
        this.meteorField = meteorField;
        this.sun = sun;
        this.warpState = warpState;
        this.scene = SceneType.NORMAL;
    }

    public void update(double deltaSeconds) {
        if (paused) {
            return;
        }
        sun = sun.moved(deltaSeconds, width);
        starField.update(deltaSeconds);
        meteorField.update(deltaSeconds);
        warpState.update(deltaSeconds);
    }

    public void moveCamera(double direction, double deltaSeconds) {
        if (paused) {
            return;
        }
        double target = cameraX + direction * CAMERA_SPEED * deltaSeconds;
        cameraX = Math.max(0.0, Math.min(width, target));
    }

    public void togglePause() {
        paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public double cameraX() {
        return cameraX;
    }

    public int width() {
        return width;
    }

    public Sun sun() {
        return sun;
    }

    public SceneType scene() {
        return scene;
    }

    public void setScene(SceneType scene) {
        this.scene = scene;
        applyScene(scene);
    }

    private void applyScene(SceneType scene) {
        switch (scene) {
            case SMALL_SUN_LEFT -> sun = new Sun(width * 0.1, height * 0.3, 10.0, 2.0);
            case RED_SUN -> sun = new Sun(sun.x(), sun.y(), sun.radius(), sun.speedX(), Sun.SunColor.RED);
            case TWO_SUNS -> sun = new Sun(width * 0.15, height * 0.3, 8.0, 1.5);
            default -> sun = sun;
        }
    }

    public List<Star> stars() {
        return starField.stars();
    }

    public List<Meteor> meteors() {
        return meteorField.meteors();
    }

    public MeteorTrail trailFor(int meteorId) {
        return meteorField.trailFor(meteorId);
    }

    public List<Explosion> explosions() {
        return meteorField.explosions();
    }

    public void switchScene() {
        SceneType[] scenes = {SceneType.SMALL_SUN_LEFT, SceneType.NO_SUN, SceneType.RED_SUN, SceneType.TWO_SUNS};
        int next = (scene.ordinal() + 1) % scenes.length;
        setScene(scenes[next]);
    }

    public WarpState warpState() {
        return warpState;
    }
}
