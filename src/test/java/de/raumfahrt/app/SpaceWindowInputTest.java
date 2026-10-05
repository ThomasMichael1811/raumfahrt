package de.raumfahrt.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.EffectDispatcher;
import de.raumfahrt.core.MeteorAppearance;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SpaceWindowInputTest {

    @Test
    void tasteDreiLoestZweitenGifAsteroidenAuchImEinmonitorModusAus() {
        MeteorField meteorField = new MeteorField(1280, 1, new MeteorSpawner(new Random(41L), 1280, 720));
        EffectDispatcher dispatcher = new EffectDispatcher();
        SpaceWindow.registerGifEffects(dispatcher, meteorField);

        assertTrue(dispatcher.trigger(3));
        assertEquals(
                MeteorAppearance.ANIMATED_GIF_2, meteorField.meteors().get(0).appearance());
    }
}
