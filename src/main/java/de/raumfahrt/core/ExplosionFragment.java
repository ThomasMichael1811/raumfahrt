package de.raumfahrt.core;

public record ExplosionFragment(
        double x,
        double y,
        double depth,
        double size,
        double speedX,
        double speedY,
        double rotation,
        double rotationSpeed) {

    public ExplosionFragment updated(double deltaSeconds) {
        return new ExplosionFragment(
                x + speedX * deltaSeconds,
                y + speedY * deltaSeconds,
                depth,
                size,
                speedX,
                speedY,
                rotation + rotationSpeed * deltaSeconds,
                rotationSpeed);
    }
}
