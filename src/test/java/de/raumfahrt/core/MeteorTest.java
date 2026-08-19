package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeteorTest {

    @Test
    void rotatedErhoehtRotationUeberDieZeit() {
        Meteor meteor = new Meteor(100, 100, 20, 0, 0, 0.5, 0.25);

        Meteor rotated = meteor.rotated(2.0);

        assertEquals(1.0, rotated.rotation(), 1e-9);
    }

    @Test
    void rotatedLaesstUebrigeDatenUnveraendert() {
        Meteor meteor = new Meteor(30, 40, 20, 5, -3, 0.5, 0.25);

        Meteor rotated = meteor.rotated(1.0);

        assertEquals(30, rotated.x());
        assertEquals(40, rotated.y());
        assertEquals(20, rotated.size());
        assertEquals(5, rotated.speedX());
        assertEquals(-3, rotated.speedY());
        assertEquals(0.25, rotated.rotationSpeed());
    }
}
