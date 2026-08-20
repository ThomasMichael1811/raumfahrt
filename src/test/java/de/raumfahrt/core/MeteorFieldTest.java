package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MeteorFieldTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

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
        field.update(1.0);
        Meteor initial = field.meteors().get(0);

        field.update(2.0);
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
}
