package de.raumfahrt.core;

import java.util.Random;

public final class MeteorSpawner {

    private static final double DEFAULT_MIN_INTERVAL = 1.5;
    private static final double DEFAULT_MAX_INTERVAL = 3.5;
    private static final double MIN_SIZE = 15.0;
    private static final double SIZE_RANGE = 30.0;
    private static final double MIN_SPEED = 60.0;
    private static final double SPEED_RANGE = 60.0;
    private static final double MIN_ROTATION_RPM = 1.0;
    private static final double ROTATION_RPM_RANGE = 9.0;
    private static final double NEAR_DEPTH_BOUND = 200.0;
    private static final double FAR_DEPTH_MAX = 900.0;
    private static final double FAR_DEPTH_MIN = 500.0;
    private static final double NEAR_DEPTH_MIN = 200.0;
    private static final double FAR_PROBABILITY = 0.7;
    private static final double SPEED_Z = 120.0;

    private final Random random;
    private final int width;
    private final int height;
    private final double minInterval;
    private final double maxInterval;

    public MeteorSpawner(Random random, int width, int height) {
        this(random, width, height, DEFAULT_MIN_INTERVAL, DEFAULT_MAX_INTERVAL);
    }

    public MeteorSpawner(Random random, int width, int height, double minInterval, double maxInterval) {
        this.random = random;
        this.width = width;
        this.height = height;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    public Meteor createMeteor() {
        double size = MIN_SIZE + random.nextDouble() * SIZE_RANGE;
        double speed = MIN_SPEED + random.nextDouble() * SPEED_RANGE;
        double speedX = random.nextBoolean() ? speed : -speed;
        double y = (random.nextDouble() - 0.5) * height;
        double rotationSpeed = (MIN_ROTATION_RPM + random.nextDouble() * ROTATION_RPM_RANGE) * 2.0 * Math.PI / 60.0;
        double x = (random.nextDouble() - 0.5) * width;
        double depth = randomDepth();
        return new Meteor(x, y, depth, size, speedX, 0, -SPEED_Z, random.nextInt(), 0, rotationSpeed);
    }

    private double randomDepth() {
        if (random.nextDouble() < FAR_PROBABILITY) {
            return FAR_DEPTH_MIN + random.nextDouble() * (FAR_DEPTH_MAX - FAR_DEPTH_MIN);
        }
        return NEAR_DEPTH_MIN + random.nextDouble() * (FAR_DEPTH_MIN - NEAR_DEPTH_MIN);
    }

    public double nextSpawnInterval() {
        return minInterval + random.nextDouble() * (maxInterval - minInterval);
    }
}
