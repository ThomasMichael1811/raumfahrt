package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MeteorFieldTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final MeteorMovement MOVEMENT = new MeteorMovement();

    @Test
    void updateSpawnedMeteoriteMitKonfigurierbarerRate() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(1L), WIDTH, HEIGHT, 0.5, 0.5);
        MeteorField field = new MeteorField(WIDTH, 5, spawner);

        field.update(1.2);

        assertEquals(2, field.meteors().size());
    }

    @Test
    void updateSpawnedNurNachAblaufDesIntervalls() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(2L), WIDTH, HEIGHT, 1.0, 1.0);
        MeteorField field = new MeteorField(WIDTH, 5, spawner);

        field.update(0.5);

        assertTrue(field.meteors().isEmpty());
    }

    @Test
    void maximalanzahlGleichzeitigerMeteoriteIstBegrenzt() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(4L), WIDTH, HEIGHT, 0.1, 0.1);
        MeteorField field = new MeteorField(WIDTH, 3, spawner);

        field.update(5.0);

        assertTrue(field.meteors().size() <= 3);
    }

    @Test
    void meteoriteBewegenSichAufDieSichtZu() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(6L), WIDTH, HEIGHT, 0.1, 0.1);
        MeteorField field = new MeteorField(WIDTH, 1, spawner);
        field.update(0.2);
        Meteor initial = field.meteors().get(0);

        field.update(0.2);
        Meteor moved = field.meteors().get(0);

        assertTrue(moved.x() != initial.x());
        assertTrue(moved.depth() < initial.depth());
        assertEquals(initial.size(), moved.size());
        assertEquals(initial.shapeSeed(), moved.shapeSeed());
        assertTrue(moved.rotation() > initial.rotation());
    }

    @Test
    void meteoriteEntferntWennFensterebeneErreicht() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(8L), WIDTH, HEIGHT, 0.0, 0.0);
        MeteorField field = new MeteorField(WIDTH, 1, spawner);

        field.update(100.0);

        assertTrue(field.meteors().isEmpty());
    }

    @Test
    void beschleunigenderMeteorWirdSchnellerJeNaeherErKommt() {
        Meteor initial = new Meteor(1, 0, 0, 400, 15, 0, 0, -120, 5, 0.0, 0.2, MeteorBehavior.ACCELERATING, 0, 0, 0);
        MeteorField field = new MeteorField(WIDTH, 1, new MeteorSpawner(new Random(10L), WIDTH, HEIGHT, 1.0, 1.0));
        double step = 2.0;

        Meteor afterFirst = MOVEMENT.move(initial, step);
        Meteor afterSecond = MOVEMENT.move(afterFirst, step);

        double firstApproach = initial.depth() - afterFirst.depth();
        double secondApproach = afterFirst.depth() - afterSecond.depth();

        assertTrue(secondApproach > firstApproach);
    }

    @Test
    void zigzagMeteorOszilliertLateral() {
        Meteor initial = new Meteor(1, 0, 0, 600, 15, 0, 0, -120, 5, 0.0, 0.2, MeteorBehavior.ZIGZAG, 50.0, 2.0, 1.0);
        MeteorField field = new MeteorField(WIDTH, 1, new MeteorSpawner(new Random(12L), WIDTH, HEIGHT, 1.0, 1.0));
        Meteor moved = MOVEMENT.move(initial, 1.0);

        assertTrue(moved.zigzagPhase() > initial.zigzagPhase());
        assertTrue(moved.x() != initial.x());
    }

    @Test
    void trailWirdFuerZigzagUndAcceleratingGefuehrt() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(14L), WIDTH, HEIGHT, 0.0, 0.0);
        MeteorField field = new MeteorField(WIDTH, 2, spawner);

        field.update(0.1);

        for (Meteor meteor : field.meteors()) {
            if (meteor.behavior() != MeteorBehavior.STRAIGHT) {
                assertTrue(field.trailFor(meteor.id()) != null);
            }
        }
    }

    @Test
    void meteorErzeugtExplosionBeimErreichenDerFensterebene() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(16L), WIDTH, HEIGHT, 0.0, 0.0);
        MeteorField field = new MeteorField(WIDTH, 1, spawner);

        field.update(10.0);

        assertTrue(field.meteors().isEmpty());
        assertTrue(field.explosions().size() >= 1);
        for (Explosion explosion : field.explosions()) {
            assertTrue(explosion.fragments().size() >= 8);
        }
    }

    @Test
    void explosionVerschwindetNachLebensdauer() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(18L), WIDTH, HEIGHT, 100.0, 100.0);
        MeteorField field = new MeteorField(WIDTH, 1, spawner);
        field.update(100.0);
        assertTrue(field.explosions().size() >= 1);

        field.update(2.0);

        assertTrue(field.explosions().isEmpty());
        assertTrue(field.meteors().isEmpty());
    }

    @Test
    void zielMeteorExplodiertAnDerFensterebene() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(20L), WIDTH, HEIGHT, 10.0, 10.0);
        MeteorField field = new MeteorField(WIDTH, 5, spawner);

        field.spawnAimedMeteor();
        assertEquals(1, field.meteors().size());

        for (int i = 0; i < 200 && !field.meteors().isEmpty(); i++) {
            field.update(0.05);
        }

        assertTrue(field.meteors().isEmpty());
        assertEquals(1, field.explosions().size());
    }
}
