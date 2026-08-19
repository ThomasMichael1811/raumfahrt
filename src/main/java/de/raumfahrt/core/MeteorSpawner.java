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

    private final Random random;
    private final int height;
    private final double minInterval;
    private final double maxInterval;

    public MeteorSpawner(Random random, int height) {
        this(random, height, DEFAULT_MIN_INTERVAL, DEFAULT_MAX_INTERVAL);
    }

    public MeteorSpawner(Random random, int height, double minInterval, double maxInterval) {
        this.random = random;
        this.height = height;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    public Meteor createMeteor() {
        double size = MIN_SIZE + random.nextDouble() * SIZE_RANGE;
        double speedX = MIN_SPEED + random.nextDouble() * SPEED_RANGE;
        double y = random.nextDouble() * height;
        double rotationSpeed = (MIN_ROTATION_RPM + random.nextDouble() * ROTATION_RPM_RANGE) * 2.0 * Math.PI / 60.0;
        return new Meteor(-size * 2, y, size, speedX, 0, random.nextInt(), 0, rotationSpeed);
    }

    public double nextSpawnInterval() {
        return minInterval + random.nextDouble() * (maxInterval - minInterval);
    }
}
