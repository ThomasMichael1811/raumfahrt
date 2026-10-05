package de.raumfahrt.core;

public final class WarpState {

    private boolean active;
    private double durationSeconds;
    private double remainingSeconds;
    private double speed;

    public WarpState() {
        this.active = false;
        this.durationSeconds = 0.0;
        this.remainingSeconds = 0.0;
        this.speed = 0.0;
    }

    public boolean active() {
        return active;
    }

    public double remainingSeconds() {
        return remainingSeconds;
    }

    public double speed() {
        return speed;
    }

    public double durationSeconds() {
        return durationSeconds;
    }

    public void activate(double durationSeconds, double speed) {
        this.active = true;
        this.durationSeconds = durationSeconds;
        this.remainingSeconds = durationSeconds;
        this.speed = speed;
    }

    public void deactivate() {
        this.active = false;
        this.remainingSeconds = 0.0;
        this.speed = 0.0;
    }

    public double progress() {
        if (!active || durationSeconds <= 0.0) {
            return 0.0;
        }
        double elapsed = durationSeconds - remainingSeconds;
        return Math.max(0.0, Math.min(1.0, elapsed / durationSeconds));
    }

    public void update(double deltaSeconds) {
        if (!active) {
            return;
        }
        remainingSeconds -= deltaSeconds;
        if (remainingSeconds <= 0.0) {
            deactivate();
        }
    }
}
