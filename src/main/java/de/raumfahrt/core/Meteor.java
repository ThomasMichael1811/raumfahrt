package de.raumfahrt.core;

public record Meteor(
        double x,
        double y,
        double size,
        double speedX,
        double speedY,
        int shapeSeed,
        double rotation,
        double rotationSpeed) {}
