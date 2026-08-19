package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MeteorFieldTest {

    private static final int WIDTH = 1280;

    @Test
    void updateBewegtMeteorEntlangDerSicht() {
        MeteorField field = new MeteorField(WIDTH, new Meteor(100, 300, 20, 60, 0, 0, 0.3));

        field.update(1.0);

        assertEquals(160, field.meteor().x(), 1e-9);
        assertEquals(300, field.meteor().y(), 1e-9);
    }

    @Test
    void updateLaesstGroesseMitAnnaeherungWachsen() {
        MeteorField field = new MeteorField(WIDTH, new Meteor(100, 300, 20, 60, 0, 0, 0.3));

        field.update(2.0);

        assertEquals(28, field.meteor().size(), 1e-9);
    }

    @Test
    void updateSetztRotationFort() {
        MeteorField field = new MeteorField(WIDTH, new Meteor(100, 300, 20, 60, 0, 0.5, 0.25));

        field.update(2.0);

        assertEquals(1.0, field.meteor().rotation(), 1e-9);
    }

    @Test
    void meteorWirdNachAustrittRechtsEntfernt() {
        MeteorField field = new MeteorField(WIDTH, new Meteor(1200, 300, 20, 70, 0, 0, 0));

        field.update(10.0);

        assertNull(field.meteor());
    }

    @Test
    void updateOhneMeteorBleibtLeer() {
        MeteorField field = new MeteorField(WIDTH, new Meteor(1200, 300, 20, 70, 0, 0, 0));
        field.update(10.0);

        field.update(1.0);

        assertNull(field.meteor());
    }
}
