package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class AsteroidFieldTest {

    @Test
    void asteroidFieldMitEinsAsteroidenErstelltWird() {
        AsteroidSpawner spawner = new AsteroidSpawner(new Random(5L), 1280, 720);
        AsteroidField asteroidField = new AsteroidField(1280, 1, spawner);

        assertEquals(0, asteroidField.asteroids().size());

        asteroidField.spawnAsteroid();
        assertEquals(1, asteroidField.asteroids().size());

        asteroidField.spawnAsteroid();
        assertEquals(2, asteroidField.asteroids().size());
    }

    @Test
    void asteroidFieldUpdateBewegtAsteroiden() {
        AsteroidSpawner spawner = new AsteroidSpawner(new Random(5L), 1280, 720);
        AsteroidField asteroidField = new AsteroidField(1280, 2, spawner);

        asteroidField.spawnAsteroid();
        asteroidField.update(0.016);
        assertTrue(asteroidField.asteroids().size() >= 1);
    }
}
