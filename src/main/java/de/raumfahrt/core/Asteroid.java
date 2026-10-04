package de.raumfahrt.core;

public record Asteroid(
        int id,
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
        double zigzagAmplitude,
        double zigzagFrequency,
        double zigzagPhase) {}
