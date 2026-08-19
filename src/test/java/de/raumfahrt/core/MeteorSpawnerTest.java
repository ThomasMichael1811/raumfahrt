package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MeteorSpawnerTest {

    private static final int HEIGHT = 720;

    @Test
    void createMeteorStartetLinksAusserhalbMitZufaelligenEigenschaften() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(5L), HEIGHT);

        Meteor meteor = spawner.createMeteor();

        assertTrue(meteor.x() < 0);
        assertTrue(meteor.y() >= 0 && meteor.y() < HEIGHT);
        assertTrue(meteor.size() >= 15 && meteor.size() <= 45);
        assertTrue(meteor.speedX() >= 60 && meteor.speedX() <= 120);
        assertTrue(meteor.rotationSpeed() == 10.0 * 2.0 * Math.PI / 3600.0);
    }

    @Test
    void gleicherSeedErzeugtGleichenMeteor() {
        MeteorSpawner first = new MeteorSpawner(new Random(9L), HEIGHT);
        MeteorSpawner second = new MeteorSpawner(new Random(9L), HEIGHT);

        assertEquals(first.createMeteor(), second.createMeteor());
    }

    @Test
    void verschiedeneAufrufeErzeugenUnterschiedlicheMeteore() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(11L), HEIGHT);

        Meteor first = spawner.createMeteor();
        Meteor second = spawner.createMeteor();

        assertTrue(first.y() != second.y() || first.size() != second.size() || first.speedX() != second.speedX());
    }

    @Test
    void spawnIntervalLiegtInnerhalbDerGrenzen() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(3L), HEIGHT, 2.0, 4.0);

        for (int i = 0; i < 100; i++) {
            double interval = spawner.nextSpawnInterval();
            assertTrue(interval >= 2.0 && interval <= 4.0);
        }
    }
}
