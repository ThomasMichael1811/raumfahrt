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

class SpacePanelPaintTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Test
    void paintComponentReihenfolgeZeichnetOhneException() {
        SimulationWorld world = neueWelt(new Random(1), new Random(2));
        for (int i = 0; i < 600; i++) {
            world.update(0.1);
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D target = image.createGraphics();
        paintSzene(target, world, new MonitorPairProjection(WIDTH, HEIGHT, 0.0, 400));
        target.dispose();

        assertTrue(nonBackgroundPixel(image) > 0, "Bild ist leer");
    }

    private SimulationWorld neueWelt(Random starRandom, Random meteorRandom) {
        StarField starField = new StarField(WIDTH, new StarGenerator().generate(WIDTH, HEIGHT, starRandom));
        MeteorField meteorField = new MeteorField(WIDTH, 4, new MeteorSpawner(meteorRandom, WIDTH, HEIGHT, 0.0, 0.0));
        Sun sun = new Sun(WIDTH, HEIGHT * 0.3, Math.min(WIDTH, HEIGHT) * 0.3, 5.0);
        return new SimulationWorld(WIDTH, starField, meteorField, sun);
    }

    private void paintSzene(Graphics2D target, SimulationWorld world, MonitorPairProjection projection) {
        new SpaceRenderer().render(target, WIDTH, HEIGHT);
        target.translate(-world.cameraX(), 0);
        new SunRenderer().render(target, world.sun());
        new StarFieldRenderer().render(target, world.stars(), WIDTH, HEIGHT);
        MeteorRenderer meteorRenderer = new MeteorRenderer();
        for (Meteor meteor : world.meteors()) {
            MeteorShape shape = new MeteorShape(meteor.shapeSeed());
            meteorRenderer.renderTrail(target, projection, meteor, shape, world.trailFor(meteor.id()));
            meteorRenderer.render(target, projection, meteor, shape);
        }
        ExplosionRenderer explosionRenderer = new ExplosionRenderer();
        for (Explosion explosion : world.explosions()) {
            explosionRenderer.render(target, projection, explosion);
        }
        target.translate(world.cameraX(), 0);
        new CabinFrameRenderer().render(target, WIDTH, HEIGHT);
    }

    private int nonBackgroundPixel(BufferedImage image) {
        int nonBackground = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (image.getRGB(x, y) != SpaceRenderer.SPACE_BACKGROUND.getRGB()) {
                    nonBackground++;
                }
            }
        }
        return nonBackground;
    }
}
