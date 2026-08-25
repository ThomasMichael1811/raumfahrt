package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SceneTypeTest {

    @Test
    void defaultSceneIsNormal() {
        SimulationWorld world = new SimulationWorld(
                1920,
                new StarField(1920, List.of()),
                new MeteorField(1920, 0, new MeteorSpawner(new java.util.Random(), 1920, 800)),
                new Sun(960, 400, 50, 5));

        assertEquals(SceneType.NORMAL, world.scene());
    }

    @Test
    void setSceneChangesScene() {
        SimulationWorld world = new SimulationWorld(
                1920,
                new StarField(1920, List.of()),
                new MeteorField(1920, 0, new MeteorSpawner(new java.util.Random(), 1920, 800)),
                new Sun(960, 400, 50, 5));

        world.setScene(SceneType.SMALL_SUN_LEFT);
        assertEquals(SceneType.SMALL_SUN_LEFT, world.scene());

        world.setScene(SceneType.NO_SUN);
        assertEquals(SceneType.NO_SUN, world.scene());

        world.setScene(SceneType.RED_SUN);
        assertEquals(SceneType.RED_SUN, world.scene());

        world.setScene(SceneType.TWO_SUNS);
        assertEquals(SceneType.TWO_SUNS, world.scene());
    }

    @Test
    void smallSunLeftPositionsSunAtLeft() {
        SimulationWorld world = new SimulationWorld(
                1920,
                new StarField(1920, List.of()),
                new MeteorField(1920, 0, new MeteorSpawner(new java.util.Random(), 1920, 800)),
                new Sun(960, 400, 50, 5));

        world.setScene(SceneType.SMALL_SUN_LEFT);

        assertEquals(192.0, world.sun().x());
        assertEquals(10.0, world.sun().radius());
    }

    @Test
    void redSunChangesColor() {
        SimulationWorld world = new SimulationWorld(
                1920,
                new StarField(1920, List.of()),
                new MeteorField(1920, 0, new MeteorSpawner(new java.util.Random(), 1920, 800)),
                new Sun(960, 400, 50, 5));

        world.setScene(SceneType.RED_SUN);

        assertEquals(Sun.SunColor.RED, world.sun().color());
    }
}
