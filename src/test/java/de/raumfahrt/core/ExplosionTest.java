package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ExplosionTest {

    private static ExplosionFragment fragmentAt(double x, double y) {
        return new ExplosionFragment(x, y, 100, 4, 10, 0, 0.0, 0.1);
    }

    @Test
    void updatedBewegtFragmenteUndErhoehtAlter() {
        Explosion explosion = new Explosion(0, 0, 100, 0.0, 1.2, List.of(fragmentAt(0, 0), fragmentAt(10, 10)));

        Explosion moved = explosion.updated(0.5);

        assertEquals(0.5, moved.age());
        assertTrue(moved.fragments().get(0).x() > 0);
        assertTrue(moved.fragments().get(1).x() > 10);
    }

    @Test
    void opazitaetSinktMitDemAlter() {
        Explosion explosion = new Explosion(0, 0, 100, 0.0, 1.2, List.of(fragmentAt(0, 0)));

        double early = explosion.opacity();
        double late = explosion.updated(0.6).opacity();

        assertTrue(late < early);
    }

    @Test
    void expiredWennLebensdauerUeberschritten() {
        Explosion fresh = new Explosion(0, 0, 100, 0.0, 1.2, List.of(fragmentAt(0, 0)));
        Explosion aged = fresh.updated(1.2);

        assertTrue(!fresh.expired());
        assertTrue(aged.expired());
    }

    @Test
    void fragmentsAuseinanderBehaltenPositionUeberDerZeit() {
        ExplosionFragment fragment = fragmentAt(0, 0);

        ExplosionFragment moved = fragment.updated(1.0);

        assertTrue(moved.x() > fragment.x());
        assertTrue(moved.rotation() > fragment.rotation());
        assertEquals(fragment.size(), moved.size());
    }
}
