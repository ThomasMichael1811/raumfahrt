package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SunTest {

    @Test
    void movedVerschiebtSonneNachLinks() {
        Sun sun = new Sun(100, 50, 30, 5.0);

        Sun moved = sun.moved(2.0, 1000);

        assertEquals(90, moved.x());
    }

    @Test
    void movedLaesstRestlicheEigenschaftenUnveraendert() {
        Sun sun = new Sun(100, 50, 30, 5.0);

        Sun moved = sun.moved(1.0, 1000);

        assertEquals(50, moved.y());
        assertEquals(30, moved.radius());
        assertEquals(5.0, moved.speedX());
    }

    @Test
    void movedTauchtNachAustrittWiederRechtsAuf() {
        Sun sun = new Sun(10, 50, 30, 5.0);

        Sun moved = sun.moved(3.0, 100);

        assertEquals(95, moved.x());
        assertTrue(moved.x() >= 0);
    }
}
