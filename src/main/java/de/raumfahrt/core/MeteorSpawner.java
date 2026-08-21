package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MeteorSpawner {

    private static final double DEFAULT_MIN_INTERVAL = 1.5;
    private static final double DEFAULT_MAX_INTERVAL = 3.5;
    private static final double MIN_SIZE = 15.0;
    private static final double SIZE_RANGE = 30.0;
    private static final double MIN_SPEED = 60.0;
    private static final double SPEED_RANGE = 60.0;
    private static final double NEAR_DEPTH_BOUND = 200.0;
    private static final double FAR_DEPTH_MAX = 900.0;
    private static final double FAR_DEPTH_MIN = 500.0;
    private static final double NEAR_DEPTH_MIN = 200.0;
    private static final double FAR_PROBABILITY = 0.7;
    private static final double SPEED_Z = 120.0;
    private static final double ZIGZAG_MIN_AMPLITUDE = 20.0;
    private static final double ZIGZAG_AMPLITUDE_RANGE = 80.0;
    private static final double ZIGZAG_MIN_FREQUENCY = 0.5;
    private static final double ZIGZAG_FREQUENCY_RANGE = 2.0;
    private static final double EFFECT_DEPTH = 600.0;

    private final Random random;
    private final int width;
    private final int height;
    private final double minInterval;
    private final double maxInterval;
    private int nextId = 1;

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
        double x = randomX();
        double y = randomY();
        double rotationSpeed = rotationSpeed();
        double depth = randomDepth();
        ZigzagOptions zigzag = randomZigzag();
        return new Meteor(
                nextId++,
                x,
                y,
                depth,
                size,
                speedX,
                0,
                -SPEED_Z,
                random.nextInt(),
                0,
                rotationSpeed,
                zigzag.behavior(),
                zigzag.amplitude(),
                zigzag.frequency(),
                0.0);
    }

    public Meteor createAimedMeteor() {
        double size = MIN_SIZE + random.nextDouble() * SIZE_RANGE;
        return new Meteor(
                nextId++,
                0.0,
                0.0,
                EFFECT_DEPTH,
                size,
                0.0,
                0.0,
                -SPEED_Z,
                random.nextInt(),
                0.0,
                rotationSpeed(),
                MeteorBehavior.STRAIGHT,
                0.0,
                0.0,
                0.0);
    }

    private double randomX() {
        return (random.nextDouble() - 0.5) * width;
    }

    private double randomY() {
        return (random.nextDouble() - 0.5) * height;
    }

    public List<ExplosionFragment> createExplosionFragments(double x, double y, double depth) {
        int count = 8 + random.nextInt(5);
        List<ExplosionFragment> fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double speed = 60.0 + random.nextDouble() * 120.0;
            double angle = random.nextDouble() * 2.0 * Math.PI;
            double size = 3.0 + random.nextDouble() * 5.0;
            double rotationSpeed = (1.0 + random.nextDouble() * 4.0) * 2.0 * Math.PI / 60.0;
            fragments.add(new ExplosionFragment(
                    x, y, depth, size, Math.cos(angle) * speed, Math.sin(angle) * speed, 0.0, rotationSpeed));
        }
        return List.copyOf(fragments);
    }

    private ZigzagOptions randomZigzag() {
        MeteorBehavior behavior = random.nextBoolean() ? MeteorBehavior.STRAIGHT : MeteorBehavior.ACCELERATING;
        if (behavior == MeteorBehavior.STRAIGHT && random.nextBoolean()) {
            double amplitude = ZIGZAG_MIN_AMPLITUDE + random.nextDouble() * ZIGZAG_AMPLITUDE_RANGE;
            double frequency = ZIGZAG_MIN_FREQUENCY + random.nextDouble() * ZIGZAG_FREQUENCY_RANGE;
            return new ZigzagOptions(MeteorBehavior.ZIGZAG, amplitude, frequency);
        }
        return new ZigzagOptions(behavior, 0.0, 0.0);
    }

    private record ZigzagOptions(MeteorBehavior behavior, double amplitude, double frequency) {}

    private double rotationSpeed() {
        double min;
        double range;
        double pick = random.nextDouble();
        if (pick < 0.33) {
            min = 1.0;
            range = 3.0;
        } else if (pick < 0.66) {
            min = 4.0;
            range = 3.0;
        } else {
            min = 7.0;
            range = 3.0;
        }
        return (min + random.nextDouble() * range) * 2.0 * Math.PI / 60.0;
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
