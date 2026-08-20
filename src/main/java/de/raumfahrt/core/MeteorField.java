package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class MeteorField {

    private static final double NEAR_PLANE = 80.0;

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

    private Meteor move(Meteor meteor, double deltaSeconds) {
        double x = meteor.x() + meteor.speedX() * deltaSeconds;
        double y = meteor.y() + meteor.speedY() * deltaSeconds;
        double depth = meteor.depth() + meteor.speedZ() * deltaSeconds;
        double rotation = meteor.rotation() + meteor.rotationSpeed() * deltaSeconds;
        return new Meteor(
                x,
                y,
                depth,
                meteor.size(),
                meteor.speedX(),
                meteor.speedY(),
                meteor.speedZ(),
                meteor.shapeSeed(),
                rotation,
                meteor.rotationSpeed());
    }

    private boolean isVisible(Meteor meteor) {
        return meteor.depth() > NEAR_PLANE && meteor.x() - meteor.size() <= width;
    }
}
