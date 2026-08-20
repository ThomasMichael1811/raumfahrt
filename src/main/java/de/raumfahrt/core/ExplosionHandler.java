package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

final class ExplosionHandler {

    private static final double EXPLOSION_LIFETIME = 1.2;

    private final List<Explosion> explosions = new ArrayList<>();
    private final List<Explosion> pending = new ArrayList<>();

    List<Explosion> explosions() {
        return List.copyOf(explosions);
    }

    void spawn(double x, double y, double depth, MeteorSpawner spawner) {
        pending.add(new Explosion(x, y, depth, 0.0, EXPLOSION_LIFETIME, spawner.createExplosionFragments(x, y, depth)));
    }

    List<Explosion> update(double deltaSeconds) {
        List<Explosion> active = new ArrayList<>();
        for (Explosion explosion : explosions) {
            Explosion updated = explosion.updated(deltaSeconds);
            if (!updated.expired()) {
                active.add(updated);
            }
        }
        active.addAll(pending);
        pending.clear();
        explosions.clear();
        explosions.addAll(active);
        return List.copyOf(explosions);
    }
}
