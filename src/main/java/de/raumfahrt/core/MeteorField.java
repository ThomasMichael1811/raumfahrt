package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class MeteorField {

    private static final double SIZE_GROWTH_PER_SECOND = 4.0;

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
            if (moved.x() - moved.size() <= width) {
                survivors.add(moved);
            }
        }
        meteors.clear();
        meteors.addAll(survivors);
    }

    private Meteor move(Meteor meteor, double deltaSeconds) {
        double x = meteor.x() + meteor.speedX() * deltaSeconds;
        double y = meteor.y() + meteor.speedY() * deltaSeconds;
        double size = meteor.size() + SIZE_GROWTH_PER_SECOND * deltaSeconds;
        return new Meteor(x, y, size, meteor.speedX(), meteor.speedY());
    }
}
