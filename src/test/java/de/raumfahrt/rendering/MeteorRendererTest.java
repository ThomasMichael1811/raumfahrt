package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class MeteorRendererTest {

    private static final int W = 100;
    private static final int H = 100;

    private static Meteor meteorAt(double worldX, double worldY, double depth, double size) {
        return new Meteor(worldX, worldY, depth, size, 0, 0, -1, 5, 0.0, 0.2);
    }

    @Test
    void renderZeichnetFelsbrockenUeberDemHintergrund() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, W, H);
        Meteor meteor = meteorAt(0, 0, 500, 15);
        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();

        assertTrue(containsRockColor(image));
    }

    @Test
    void renderZeichnetBrockenAnProjizierterPosition() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, W, H);
        Meteor meteor = meteorAt(0, 0, 500, 15);
        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();

        assertTrue(rockAtPosition(image, W / 2, H / 2));
    }

    @Test
    void naherMeteorWirktGroesser() {
        BufferedImage nearImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D nearGraphics = nearImage.createGraphics();
        new SpaceRenderer().render(nearGraphics, W, H);
        new MeteorRenderer()
                .render(
                        nearGraphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 200, 15),
                        new MeteorShape(5));
        nearGraphics.dispose();

        BufferedImage farImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D farGraphics = farImage.createGraphics();
        new SpaceRenderer().render(farGraphics, W, H);
        new MeteorRenderer()
                .render(
                        farGraphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 800, 15),
                        new MeteorShape(5));
        farGraphics.dispose();

        assertTrue(countRockPixels(nearImage) > countRockPixels(farImage));
    }

    private boolean rockAtPosition(BufferedImage image, int x, int y) {
        return image.getRGB(x, y) == MeteorRenderer.METEOR_COLOR.getRGB();
    }

    private boolean containsRockColor(BufferedImage image) {
        return countRockPixels(image) > 0;
    }

    private int countRockPixels(BufferedImage image) {
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == MeteorRenderer.METEOR_COLOR.getRGB()) {
                    count++;
                }
            }
        }
        return count;
    }
}
