package de.raumfahrt.core;

import java.util.List;

public record Explosion(
        double x, double y, double depth, double age, double lifetime, List<ExplosionFragment> fragments) {

    public Explosion updated(double deltaSeconds) {
        return new Explosion(
                x,
                y,
                depth,
                age + deltaSeconds,
                lifetime,
                fragments.stream()
                        .map(fragment -> fragment.updated(deltaSeconds))
                        .toList());
    }

    public double opacity() {
        return Math.max(0.0, 1.0 - age / lifetime);
    }

    public boolean expired() {
        return age >= lifetime;
    }
}
