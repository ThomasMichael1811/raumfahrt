package de.raumfahrt.core;

public final class WarpState {

    private boolean active;
    private double remainingSeconds;
    private double speed;

    public WarpState() {
        this.active = false;
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

    public void activate(double durationSeconds, double speed) {
        this.active = true;
        this.remainingSeconds = durationSeconds;
        this.speed = speed;
    }

    public void deactivate() {
        this.active = false;
        this.remainingSeconds = 0.0;
        this.speed = 0.0;
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
