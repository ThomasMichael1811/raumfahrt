package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class AsteroidField {

    private static final double NEAR_PLANE = 50.0;
    private static final double TRAIL_LENGTH_FACTOR = 2.0;

    private final int width;
    private final int maxAsteroids;
    private final AsteroidSpawner spawner;
    private final List<Asteroid> asteroids = new ArrayList<>();
    private final List<AsteroidTrail> trails = new ArrayList<>();
    private double spawnTimer;
    private double nextSpawnInterval;

    public AsteroidField(int width, int maxAsteroids, AsteroidSpawner spawner) {
        this.width = width;
        this.maxAsteroids = maxAsteroids;
        this.spawner = spawner;
        this.nextSpawnInterval = spawner.nextSpawnInterval();
        this.spawnTimer = 0.0;
    }

    public List<Asteroid> asteroids() {
        return List.copyOf(asteroids);
    }

    public void spawnAsteroid() {
        Asteroid asteroid = spawner.createAsteroid();
        asteroids.add(asteroid);
        if (hasTrail(asteroid)) {
            trails.add(new AsteroidTrail((int) (TRAIL_LENGTH_FACTOR * width)));
        }
    }

    public void update(double deltaSeconds) {
        spawnAsteroids(deltaSeconds);
        List<Asteroid> survivors = moveAsteroids(deltaSeconds);
        asteroids.clear();
        asteroids.addAll(survivors);
    }

    private void spawnAsteroids(double deltaSeconds) {
        spawnTimer += deltaSeconds;
        while (spawnTimer >= nextSpawnInterval && asteroids.size() < maxAsteroids) {
            spawnTimer -= nextSpawnInterval;
            nextSpawnInterval = spawner.nextSpawnInterval();
            spawnAsteroid();
        }
    }

    private List<Asteroid> moveAsteroids(double deltaSeconds) {
        List<Asteroid> survivors = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            if (asteroid.depth() <= NEAR_PLANE) {
                trails.removeIf(t -> t.asteroidId() == asteroid.id());
            } else if (isVisible(asteroid)) {
                survivors.add(asteroid);
                AsteroidTrail trail = getTrail(asteroid.id());
                if (trail != null) {
                    trail.push(
                            new AsteroidTrail.TrailPoint(asteroid.x(), asteroid.y(), asteroid.depth()),
                            distance(asteroid));
                }
            } else {
                trails.removeIf(t -> t.asteroidId() == asteroid.id());
            }
        }
        return survivors;
    }

    private boolean hasTrail(Asteroid asteroid) {
        return asteroid.zigzagAmplitude() > 0.0 || asteroid.zigzagFrequency() > 0.0;
    }

    private double distance(Asteroid asteroid) {
        return Math.hypot(asteroid.x(), asteroid.y());
    }

    private boolean isVisible(Asteroid asteroid) {
        return asteroid.depth() > NEAR_PLANE && Math.abs(asteroid.x()) - asteroid.size() <= width;
    }

    public AsteroidTrail getTrail(int asteroidId) {
        return trails.stream()
                .filter(t -> t.asteroidId() == asteroidId)
                .findFirst()
                .orElse(null);
    }

    public static class AsteroidTrail {

        private static final double TRAIL_DECAY = 0.95;

        private final int asteroidId;
        private final List<TrailPoint> points = new ArrayList<>();
        private double opacity = 1.0;

        public AsteroidTrail(int asteroidId) {
            this.asteroidId = asteroidId;
        }

        public int asteroidId() {
            return asteroidId;
        }

        public void push(TrailPoint point, double distance) {
            points.add(point);
            if (points.size() > 50) {
                points.remove(0);
            }
            opacity *= TRAIL_DECAY;
        }

        public double opacity() {
            return opacity;
        }

        public List<TrailPoint> points() {
            return List.copyOf(points);
        }

        public static record TrailPoint(double x, double y, double depth) {}
    }
}
