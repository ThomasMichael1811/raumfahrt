package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MeteorField {

    private static final double NEAR_PLANE = 80.0;
    private static final double TRAIL_LENGTH_FACTOR = 2.0;

    private final int width;
    private final int maxMeteors;
    private final MeteorSpawner spawner;
    private final List<Meteor> meteors = new ArrayList<>();
    private final Map<Integer, MeteorTrail> trails = new HashMap<>();
    private final ExplosionHandler explosionHandler = new ExplosionHandler();
    private final MeteorMovement movement = new MeteorMovement();
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

    public List<Explosion> explosions() {
        return explosionHandler.explosions();
    }

    public MeteorTrail trailFor(int meteorId) {
        return trails.get(meteorId);
    }

    public void update(double deltaSeconds) {
        spawnMeteors(deltaSeconds);
        List<Meteor> survivors = moveMeteors(deltaSeconds);
        meteors.clear();
        meteors.addAll(survivors);
        explosionHandler.update(deltaSeconds);
    }

    private void spawnMeteors(double deltaSeconds) {
        spawnTimer += deltaSeconds;
        while (spawnTimer >= nextSpawnInterval && meteors.size() < maxMeteors) {
            spawnTimer -= nextSpawnInterval;
            nextSpawnInterval = spawner.nextSpawnInterval();
            Meteor meteor = spawner.createMeteor();
            meteors.add(meteor);
            if (hasTrail(meteor)) {
                trails.put(meteor.id(), new MeteorTrail(TRAIL_LENGTH_FACTOR * width));
            }
        }
    }

    private List<Meteor> moveMeteors(double deltaSeconds) {
        List<Meteor> survivors = new ArrayList<>();
        for (Meteor meteor : meteors) {
            Meteor moved = movement.move(meteor, deltaSeconds);
            if (moved.depth() <= NEAR_PLANE) {
                explosionHandler.spawn(moved.x(), moved.y(), moved.depth(), spawner);
                trails.remove(moved.id());
                continue;
            }
            if (isVisible(moved)) {
                survivors.add(moved);
                MeteorTrail trail = trails.get(moved.id());
                if (trail != null) {
                    trail.push(
                            new MeteorTrail.TrailPoint(moved.x(), moved.y(), moved.depth()), distance(meteor, moved));
                }
            } else {
                trails.remove(moved.id());
            }
        }
        return survivors;
    }

    private boolean hasTrail(Meteor meteor) {
        return meteor.behavior() == MeteorBehavior.ZIGZAG || meteor.behavior() == MeteorBehavior.ACCELERATING;
    }

    private double distance(Meteor from, Meteor to) {
        double dx = to.x() - from.x();
        double dy = to.y() - from.y();
        double dz = to.depth() - from.depth();
        return Math.hypot(dx, Math.hypot(dy, dz));
    }

    private boolean isVisible(Meteor meteor) {
        return meteor.depth() > NEAR_PLANE && Math.abs(meteor.x()) - meteor.size() <= width;
    }
}
