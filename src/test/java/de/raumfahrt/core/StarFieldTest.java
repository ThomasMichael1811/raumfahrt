package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarFieldTest {

    private static final int WIDTH = 1280;

    @Test
    void updateBewegtSterneNachLinks() {
        Star star = new Star(100, 50, 1.0, 1.0, 1);
        StarField field = new StarField(WIDTH, List.of(star));

        field.update(1.0);

        assertEquals(55, field.stars().get(0).x(), 1e-9);
    }

    @Test
    void naheSterneBewegenSichSchnellerAlsFerne() {
        StarField field = new StarField(WIDTH, List.of(
            new Star(300, 10, 1.0, 1.0, 0),
            new Star(300, 20, 1.0, 1.0, 1),
            new Star(300, 30, 1.0, 1.0, 2)));

        field.update(1.0);

        List<Star> moved = field.stars();
        assertTrue(moved.get(2).x() < moved.get(1).x());
        assertTrue(moved.get(1).x() < moved.get(0).x());
    }

    @Test
    void ausgelaufeneSterneWerdenAnGegenueberliegenderSeiteErsetzt() {
        Star star = new Star(5, 50, 1.0, 1.0, 2);
        StarField field = new StarField(WIDTH, List.of(star));

        field.update(1.0);

        assertEquals(1195, field.stars().get(0).x(), 1e-9);
        assertTrue(field.stars().get(0).x() >= 0);
    }

    @Test
    void updateNutztDeltaTimeProportional() {
        Star star = new Star(300, 50, 1.0, 1.0, 1);
        StarField field = new StarField(WIDTH, List.of(star));

        field.update(2.0);

        assertEquals(210, field.stars().get(0).x(), 1e-9);
    }
}
