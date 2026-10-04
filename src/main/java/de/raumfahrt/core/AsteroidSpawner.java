package de.raumfahrt.core;

import java.util.Random;

public final class AsteroidSpawner {

    public static final double DEFAULT_FOCAL_PX = 400.0;
    private static final double DEFAULT_MIN_INTERVAL = 2.0;
    private static final double DEFAULT_MAX_INTERVAL = 5.0;
    private static final double MIN_SIZE = 20.0;
    private static final double SIZE_RANGE = 40.0;
    private static final double MIN_SPEED = 40.0;
    private static final double SPEED_RANGE = 80.0;
    private static final double NEAR_DEPTH_BOUND = 100.0;
    private static final double FAR_DEPTH_MAX = 800.0;
    private static final double FAR_DEPTH_MIN = 400.0;
    private static final double FAR_PROBABILITY = 0.8;
    private static final double SPEED_Z = 100.0;
    private static final double ZIGZAG_MIN_AMPLITUDE = 30.0;
    private static final double ZIGZAG_AMPLITUDE_RANGE = 70.0;
    private static final double ZIGZAG_MIN_FREQUENCY = 0.3;
    private static final double ZIGZAG_FREQUENCY_RANGE = 2.5;
    private static final double EFFECT_DEPTH = 500.0;
    private static final double SPAWN_MARGIN_PX = 80.0;
    private static final double ROTATION_SPEED_SCALE = 2.0;

    private final Random random;
    private final int width;
    private final int height;
    private final double minInterval;
    private final double maxInterval;
    private final double focalPx;
    private int nextId = 1;

    public AsteroidSpawner(Random random, int width, int height) {
        this(random, width, height, DEFAULT_FOCAL_PX);
    }

    public AsteroidSpawner(Random random, int width, int height, double focalPx) {
        if (focalPx <= 0) {
            throw new IllegalArgumentException("Focal muss positiv sein: " + focalPx);
        }
        this.random = random;
        this.width = width;
        this.height = height;
        this.minInterval = DEFAULT_MIN_INTERVAL;
        this.maxInterval = DEFAULT_MAX_INTERVAL;
        this.focalPx = focalPx;
    }

    public AsteroidSpawner(Random random, int width, int height, double minInterval, double maxInterval) {
        this.random = random;
        this.width = width;
        this.height = height;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.focalPx = DEFAULT_FOCAL_PX;
    }

    public Asteroid createAsteroid() {
        double size = MIN_SIZE + random.nextDouble() * SIZE_RANGE;
        double speed = MIN_SPEED + random.nextDouble() * SPEED_RANGE;
        double speedX = random.nextBoolean() ? speed : -speed;
        double rotationSpeed = rotationSpeed();
        double depth = randomDepth();
        double x = randomScreenOffset(width) * depth / focalPx;
        double y = randomScreenOffset(height) * depth / focalPx;
        return new Asteroid(
                nextId++, x, y, depth, size, speedX, 0, -SPEED_Z, random.nextInt(), 0, rotationSpeed, 0.0, 0.0, 0.0);
    }

    private double randomScreenOffset(int extentPx) {
        double halfExtent = extentPx / 2.0 - SPAWN_MARGIN_PX;
        return (random.nextDouble() * 2.0 - 1.0) * halfExtent;
    }

    private double rotationSpeed() {
        double min;
        double range;
        double pick = random.nextDouble();
        if (pick < 0.33) {
            min = 1.0;
            range = 2.0;
        } else if (pick < 0.66) {
            min = 3.0;
            range = 2.0;
        } else {
            min = 5.0;
            range = 2.0;
        }
        return (min + random.nextDouble() * range) * 2.0 * Math.PI / 60.0 * ROTATION_SPEED_SCALE;
    }

    private double randomDepth() {
        if (random.nextDouble() < FAR_PROBABILITY) {
            return FAR_DEPTH_MIN + random.nextDouble() * (FAR_DEPTH_MAX - FAR_DEPTH_MIN);
        }
        return NEAR_DEPTH_BOUND;
    }

    public double nextSpawnInterval() {
        return minInterval + random.nextDouble() * (maxInterval - minInterval);
    }
}
