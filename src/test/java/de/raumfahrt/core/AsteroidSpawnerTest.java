package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class AsteroidSpawnerTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Test
    void createAsteroidErzeugtAsteroidIm3dRaum() {
        AsteroidSpawner spawner = new AsteroidSpawner(new Random(5L), WIDTH, HEIGHT);

        Asteroid asteroid = spawner.createAsteroid();

        assertTrue(Math.abs(asteroid.size()) >= 20 && asteroid.size() <= 60);
        assertTrue(Math.abs(asteroid.speedX()) >= 40 && Math.abs(asteroid.speedX()) <= 120);
        assertTrue(asteroid.rotation() == 0.0);
        assertTrue(asteroid.rotationSpeed() >= 1.0 * 2.0 * Math.PI / 60.0
                && asteroid.rotationSpeed() <= 10.0 * 2.0 * Math.PI / 60.0 * 2.0);
        assertTrue(asteroid.depth() >= 100 && asteroid.depth() <= 800);
        assertTrue(asteroid.speedZ() < 0);
    }

    @Test
    void createAsteroidMitZufallsseedDifferent() {
        AsteroidSpawner spawner1 = new AsteroidSpawner(new Random(1L), WIDTH, HEIGHT);
        AsteroidSpawner spawner2 = new AsteroidSpawner(new Random(2L), WIDTH, HEIGHT);

        Asteroid asteroid1 = spawner1.createAsteroid();
        Asteroid asteroid2 = spawner2.createAsteroid();

        assertTrue(asteroid1.size() != asteroid2.size());
        assertTrue(asteroid1.depth() != asteroid2.depth());
    }

    @Test
    void nextSpawnIntervalImGrundbereich() {
        AsteroidSpawner spawner = new AsteroidSpawner(new Random(5L), WIDTH, HEIGHT);
        double interval = spawner.nextSpawnInterval();

        assertTrue(interval >= 2.0 && interval <= 5.0);
    }
}
