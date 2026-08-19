package de.raumfahrt.core;

public record Meteor(double x, double y, double size, double speedX, double speedY,
                     double rotation, double rotationSpeed) {

    public Meteor rotated(double deltaSeconds) {
        return new Meteor(x, y, size, speedX, speedY,
            rotation + rotationSpeed * deltaSeconds, rotationSpeed);
    }
}
