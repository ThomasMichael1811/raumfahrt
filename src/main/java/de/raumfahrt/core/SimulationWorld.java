package de.raumfahrt.core;

import java.util.List;

public final class SimulationWorld {

    private static final double CAMERA_SPEED = 120.0;

    private final int width;
    private final StarField starField;
    private final MeteorField meteorField;
    private Sun sun;
    private double cameraX;
    private boolean paused;

    public SimulationWorld(int width, StarField starField, MeteorField meteorField, Sun sun) {
        this.width = width;
        this.starField = starField;
        this.meteorField = meteorField;
        this.sun = sun;
    }

    public void update(double deltaSeconds) {
        if (paused) {
            return;
        }
        sun = sun.moved(deltaSeconds, width);
        starField.update(deltaSeconds);
        meteorField.update(deltaSeconds);
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

    public List<Star> stars() {
        return starField.stars();
    }

    public List<Meteor> meteors() {
        return meteorField.meteors();
    }

    public MeteorTrail trailFor(int meteorId) {
        return meteorField.trailFor(meteorId);
    }
}
