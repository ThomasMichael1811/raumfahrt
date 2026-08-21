package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MeteorSpawnerTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Test
    void createMeteorErzeugtMeteorIm3dRaum() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(5L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createMeteor();

        assertTrue(Math.abs(meteor.x()) <= WIDTH / 2.0);
        assertTrue(meteor.y() >= -HEIGHT / 2.0 && meteor.y() < HEIGHT / 2.0);
        assertTrue(meteor.size() >= 15 && meteor.size() <= 45);
        assertTrue(Math.abs(meteor.speedX()) >= 60 && Math.abs(meteor.speedX()) <= 120);
        assertTrue(meteor.rotation() == 0.0);
        assertTrue(meteor.rotationSpeed() >= 1.0 * 2.0 * Math.PI / 60.0
                && meteor.rotationSpeed() <= 10.0 * 2.0 * Math.PI / 60.0);
        assertTrue(meteor.depth() >= 200 && meteor.depth() <= 900);
        assertTrue(meteor.speedZ() < 0);
    }

    @Test
    void rotationsgeschwindigkeitVariiertZwischenMeteoren() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(13L), WIDTH, HEIGHT);

        double first = spawner.createMeteor().rotationSpeed();
        double second = spawner.createMeteor().rotationSpeed();

        assertTrue(first != second);
    }

    @Test
    void gleicherSeedErzeugtGleichenMeteor() {
        MeteorSpawner first = new MeteorSpawner(new Random(9L), WIDTH, HEIGHT);
        MeteorSpawner second = new MeteorSpawner(new Random(9L), WIDTH, HEIGHT);

        assertEquals(first.createMeteor(), second.createMeteor());
    }

    @Test
    void verschiedeneAufrufeErzeugenUnterschiedlicheMeteore() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(11L), WIDTH, HEIGHT);

        Meteor first = spawner.createMeteor();
        Meteor second = spawner.createMeteor();

        assertTrue(first.y() != second.y() || first.size() != second.size() || first.speedX() != second.speedX());
    }

    @Test
    void spawnIntervalLiegtInnerhalbDerGrenzen() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(3L), WIDTH, HEIGHT, 2.0, 4.0);

        for (int i = 0; i < 100; i++) {
            double interval = spawner.nextSpawnInterval();
            assertTrue(interval >= 2.0 && interval <= 4.0);
        }
    }

    @Test
    void tiefenverteilungHatFernenSchwerpunkt() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(7L), WIDTH, HEIGHT);
        int farCount = 0;
        int total = 200;

        for (int i = 0; i < total; i++) {
            if (spawner.createMeteor().depth() >= 500.0) {
                farCount++;
            }
        }

        assertTrue(farCount > total / 2);
    }

    @Test
    void explosionsfragmenteFliegenVonDerPositionWeg() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(15L), WIDTH, HEIGHT);

        java.util.List<ExplosionFragment> fragments = spawner.createExplosionFragments(0, 0, 100);

        assertTrue(fragments.size() >= 8);
        for (ExplosionFragment fragment : fragments) {
            assertTrue(fragment.x() == 0.0 && fragment.y() == 0.0);
            assertTrue(fragment.speedX() != 0.0 || fragment.speedY() != 0.0);
            assertTrue(fragment.size() >= 3 && fragment.size() <= 8);
        }
    }

    @Test
    void zielMeteorFliegtGeradeAufsFensterZu() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(17L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createAimedMeteor();

        assertEquals(0.0, meteor.x(), 1e-9);
        assertEquals(0.0, meteor.y(), 1e-9);
        assertEquals(0.0, meteor.speedX(), 1e-9);
        assertEquals(0.0, meteor.speedY(), 1e-9);
        assertTrue(meteor.speedZ() < 0);
        assertEquals(MeteorBehavior.STRAIGHT, meteor.behavior());
        assertTrue(meteor.depth() >= 200 && meteor.depth() <= 900);
    }
}
