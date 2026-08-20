package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MeteorTrailTest {

    @Test
    void pushHaengtNeuePunkteAnUndBewahrtReihenfolge() {
        MeteorTrail trail = new MeteorTrail(1000.0);
        trail.push(new MeteorTrail.TrailPoint(0, 0, 0), 1.0);
        trail.push(new MeteorTrail.TrailPoint(10, 0, 0), 10.0);
        trail.push(new MeteorTrail.TrailPoint(20, 0, 0), 10.0);

        assertEquals(3, trail.size());
        assertEquals(0.0, trail.points().get(0).x());
        assertEquals(20.0, trail.points().get(2).x());
    }

    @Test
    void laengeWirdAufMaxLengthBegrenzt() {
        MeteorTrail trail = new MeteorTrail(30.0);
        for (int i = 0; i < 100; i++) {
            trail.push(new MeteorTrail.TrailPoint(i * 2.0, 0, 0), 2.0);
        }

        assertTrue(trail.size() < 100);
        assertEquals(198.0, trail.points().get(trail.size() - 1).x());
        assertTrue(trail.points().get(0).x() >= 168.0);
    }

    @Test
    void opazitaetVerblasstZumEndeHin() {
        MeteorTrail trail = new MeteorTrail(1000.0);
        for (int i = 0; i < 10; i++) {
            trail.push(new MeteorTrail.TrailPoint(i, 0, 0), 1.0);
        }

        assertTrue(trail.opacityAt(0) > trail.opacityAt(trail.size() - 1));
        assertEquals(0.35, trail.headOpacity(), 1e-9);
        assertTrue(trail.opacityAt(trail.size() - 1) < trail.opacityAt(0));
    }
}
