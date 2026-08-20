package de.raumfahrt.core;

public record Meteor(
        double x,
        double y,
        double depth,
        double size,
        double speedX,
        double speedY,
        double speedZ,
        int shapeSeed,
        double rotation,
        double rotationSpeed,
        MeteorBehavior behavior,
        double zigzagAmplitude,
        double zigzagFrequency,
        double zigzagPhase) {}
