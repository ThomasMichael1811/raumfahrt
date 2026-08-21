package de.raumfahrt.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Explosion;
import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.MonitorPairProjection;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.core.Sun;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.ExplosionRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import de.raumfahrt.rendering.SunRenderer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SpacePanelPaintStressTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FRAMES = 2000;

    @Test
    void vielePaintZyklenOhneException() {
        SimulationWorld world = neueWelt();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        MonitorPairProjection projection = new MonitorPairProjection(WIDTH, HEIGHT, 0.0, 400);
        MeteorRenderer meteorRenderer = new MeteorRenderer();
        ExplosionRenderer explosionRenderer = new ExplosionRenderer();

        Throwable crash = null;
        for (int frame = 0; frame < FRAMES && crash == null; frame++) {
            world.update(0.05);
            crash = paintFrame(image, world, projection, meteorRenderer, explosionRenderer);
        }
        assertTrue(crash == null, "Paint-Crash: " + crash);
    }

    private SimulationWorld neueWelt() {
        StarField starField = new StarField(WIDTH, new StarGenerator().generate(WIDTH, HEIGHT, new Random()));
        MeteorField meteorField = new MeteorField(WIDTH, 4, new MeteorSpawner(new Random(), WIDTH, HEIGHT));
        Sun sun = new Sun(WIDTH, HEIGHT * 0.3, Math.min(WIDTH, HEIGHT) * 0.3, 5.0);
        return new SimulationWorld(WIDTH, starField, meteorField, sun);
    }

    private Throwable paintFrame(
            BufferedImage image,
            SimulationWorld world,
            MonitorPairProjection projection,
            MeteorRenderer meteorRenderer,
            ExplosionRenderer explosionRenderer) {
        try {
            Graphics2D target = image.createGraphics();
            new SpaceRenderer().render(target, WIDTH, HEIGHT);
            target.translate(-world.cameraX(), 0);
            new SunRenderer().render(target, world.sun());
            new StarFieldRenderer().render(target, world.stars(), WIDTH, HEIGHT);
            for (Meteor meteor : world.meteors()) {
                MeteorShape shape = new MeteorShape(meteor.shapeSeed());
                meteorRenderer.renderTrail(target, projection, meteor, shape, world.trailFor(meteor.id()));
                meteorRenderer.render(target, projection, meteor, shape);
            }
            for (Explosion explosion : world.explosions()) {
                explosionRenderer.render(target, projection, explosion);
            }
            target.translate(world.cameraX(), 0);
            new CabinFrameRenderer().render(target, WIDTH, HEIGHT);
            target.dispose();
            return null;
        } catch (Throwable throwable) {
            return throwable;
        }
    }
}
