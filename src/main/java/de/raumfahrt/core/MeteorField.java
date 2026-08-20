package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MeteorField {

    private static final double NEAR_PLANE = 80.0;
    private static final double ACCELERATION_BASE_DEPTH = 500.0;
    private static final double TRAIL_LENGTH_FACTOR = 2.0;

    private final int width;
    private final int maxMeteors;
    private final MeteorSpawner spawner;
    private final List<Meteor> meteors = new ArrayList<>();
    private final Map<Integer, MeteorTrail> trails = new HashMap<>();
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

    public MeteorTrail trailFor(int meteorId) {
        return trails.get(meteorId);
    }

    public void update(double deltaSeconds) {
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
        List<Meteor> survivors = new ArrayList<>();
        for (Meteor meteor : meteors) {
            Meteor moved = move(meteor, deltaSeconds);
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
        meteors.clear();
        meteors.addAll(survivors);
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

    Meteor move(Meteor meteor, double deltaSeconds) {
        BehaviorStep step = behaviorStep(meteor, deltaSeconds);
        return new Meteor(
                meteor.id(),
                meteor.x() + step.deltaX(),
                meteor.y() + step.deltaY(),
                meteor.depth() + step.deltaZ(),
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
                step.phase());
    }

    private BehaviorStep behaviorStep(Meteor meteor, double deltaSeconds) {
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
        return new BehaviorStep(deltaX, deltaY, deltaZ, phase);
    }

    private record BehaviorStep(double deltaX, double deltaY, double deltaZ, double phase) {}

    private boolean isVisible(Meteor meteor) {
        return meteor.depth() > NEAR_PLANE && Math.abs(meteor.x()) - meteor.size() <= width;
    }
}
