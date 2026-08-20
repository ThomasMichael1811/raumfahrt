package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class MeteorField {

    private static final double NEAR_PLANE = 80.0;
    private static final double ACCELERATION_BASE_DEPTH = 500.0;

    private final int width;
    private final int maxMeteors;
    private final MeteorSpawner spawner;
    private final List<Meteor> meteors = new ArrayList<>();
    private double spawnTimer;
    private double nextSpawnInterval;

    public MeteorField(int width, int maxMeteors, MeteorSpawner spawner) {
        this.width = width;
        this.maxMeteors = maxMeteors;
        this.spawner = spawner;
        this.nextSpawnInterval = spawner.nextSpawnInterval();
    }

    public List<Meteor> meteors() {
        return List.copyOf(meteors);
    }

    public void update(double deltaSeconds) {
        spawnTimer += deltaSeconds;
        while (spawnTimer >= nextSpawnInterval && meteors.size() < maxMeteors) {
            spawnTimer -= nextSpawnInterval;
            nextSpawnInterval = spawner.nextSpawnInterval();
            meteors.add(spawner.createMeteor());
        }
        List<Meteor> survivors = new ArrayList<>();
        for (Meteor meteor : meteors) {
            Meteor moved = move(meteor, deltaSeconds);
            if (isVisible(moved)) {
                survivors.add(moved);
            }
        }
        meteors.clear();
        meteors.addAll(survivors);
    }

    Meteor move(Meteor meteor, double deltaSeconds) {
        double deltaX = meteor.speedX() * deltaSeconds;
        double deltaY = meteor.speedY() * deltaSeconds;
        double deltaZ = meteor.speedZ() * deltaSeconds;
        double phase = meteor.zigzagPhase();
        if (meteor.behavior() == MeteorBehavior.ACCELERATING) {
            double factor = Math.max(1.0, ACCELERATION_BASE_DEPTH / meteor.depth());
            deltaX *= factor;
            deltaY *= factor;
            deltaZ *= factor;
        } else if (meteor.behavior() == MeteorBehavior.ZIGZAG) {
            deltaX += Math.sin(phase) * meteor.zigzagAmplitude() * deltaSeconds;
            phase += meteor.zigzagFrequency() * deltaSeconds;
        }
        return new Meteor(
                meteor.x() + deltaX,
                meteor.y() + deltaY,
                meteor.depth() + deltaZ,
                meteor.size(),
                meteor.speedX(),
                meteor.speedY(),
                meteor.speedZ(),
                meteor.shapeSeed(),
                meteor.rotation() + meteor.rotationSpeed() * deltaSeconds,
                meteor.rotationSpeed(),
                meteor.behavior(),
                meteor.zigzagAmplitude(),
                meteor.zigzagFrequency(),
                phase);
    }

    private boolean isVisible(Meteor meteor) {
        return meteor.depth() > NEAR_PLANE && Math.abs(meteor.x()) - meteor.size() <= width;
    }
}
