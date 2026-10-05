package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorAppearance;
import de.raumfahrt.core.MeteorBehavior;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MeteorTrail;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class MeteorRendererTest {

    private static final int W = 100;
    private static final int H = 100;

    private static Meteor meteorAt(double worldX, double worldY, double depth, double size) {
        return new Meteor(
                1,
                worldX,
                worldY,
                depth,
                size,
                0,
                0,
                -1,
                5,
                0.0,
                0.2,
                MeteorBehavior.STRAIGHT,
                0,
                0,
                0,
                MeteorAppearance.DEFAULT);
    }

    @Test
    void renderZeichnetFelsbrockenUeberDemHintergrund() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Meteor meteor = meteorAt(0, 0, 500, 15);
        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();

        assertTrue(countTexturedPixels(image) > 0);
    }

    @Test
    void renderZeichnetBrockenAnProjizierterPosition() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Meteor meteor = meteorAt(0, 0, 500, 15);
        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();

        assertTrue(image.getRGB(W / 2, H / 2) >>> 24 > 0);
    }

    @Test
    void naherMeteorWirktGroesser() {
        BufferedImage nearImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D nearGraphics = nearImage.createGraphics();
        new MeteorRenderer()
                .render(
                        nearGraphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 200, 15),
                        new MeteorShape(5));
        nearGraphics.dispose();

        BufferedImage farImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D farGraphics = farImage.createGraphics();
        new MeteorRenderer()
                .render(
                        farGraphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 800, 15),
                        new MeteorShape(5));
        farGraphics.dispose();

        assertTrue(countTexturedPixels(nearImage) > countTexturedPixels(farImage));
    }

    @Test
    void renderTrailZeichnetOhneColorException() {
        MeteorTrail trail = new MeteorTrail(1000.0);
        trail.push(new MeteorTrail.TrailPoint(-20, 0, 600), 20.0);
        trail.push(new MeteorTrail.TrailPoint(-10, 0, 550), 10.0);
        trail.push(new MeteorTrail.TrailPoint(0, 0, 500), 10.0);

        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, W, H);
        graphics.dispose();
        int baseline = countTrailPixels(image);

        Graphics2D trailGraphics = image.createGraphics();
        new MeteorRenderer()
                .renderTrail(
                        trailGraphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 500, 15),
                        new MeteorShape(5),
                        trail);
        trailGraphics.dispose();

        assertTrue(countTrailPixels(image) > baseline);
    }

    @Test
    void schweifWirdAlsPixelblockGezeichnet() {
        MeteorTrail trail = new MeteorTrail(1000.0);
        trail.push(new MeteorTrail.TrailPoint(-20, 0, 600), 20.0);
        trail.push(new MeteorTrail.TrailPoint(-10, 0, 550), 10.0);
        trail.push(new MeteorTrail.TrailPoint(0, 0, 500), 10.0);

        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new MeteorRenderer()
                .renderTrail(
                        graphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteorAt(0, 0, 500, 15),
                        new MeteorShape(5),
                        trail);
        graphics.dispose();

        assertTrue(coloredRowsAt(image, W / 2) >= MeteorRenderer.PIXEL_SIZE);
    }

    @Test
    void linkerMonitorRendertProjiziertLokal() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Meteor meteor = meteorAt(-62.5, 0, 500, 15);
        new MeteorRenderer()
                .render(
                        graphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteor,
                        new MeteorShape(5),
                        MonitorView.LEFT);
        graphics.dispose();

        assertTrue(image.getRGB(W / 2, H / 2) >>> 24 > 0);
    }

    @Test
    void rechterMonitorRendertProjiziertLokal() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Meteor meteor = meteorAt(62.5, 0, 500, 15);
        new MeteorRenderer()
                .render(
                        graphics,
                        new MonitorPairProjection(W, H, 0, 400),
                        meteor,
                        new MeteorShape(5),
                        MonitorView.RIGHT);
        graphics.dispose();

        assertTrue(image.getRGB(W / 2, H / 2) >>> 24 > 0);
    }

    @Test
    void renderDrehtSpriteUmBildmitte() {
        BufferedImage ungedreht = renderMeteor(0.0);
        BufferedImage gedreht = renderMeteor(Math.PI / 2.0);

        assertTrue(differentPixels(ungedreht, gedreht) > 0);
    }

    @Test
    void animatedGifWirdAnProjizierterPositionGezeichnet() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, W, H);
        Meteor meteor = new Meteor(
                1, 0, 0, 500, 30, 0, 0, -120, 5, 0, 0, MeteorBehavior.STRAIGHT, 0, 0, 0, MeteorAppearance.ANIMATED_GIF);

        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();

        assertTrue(countNonBackgroundPixels(image) > 0);
        assertEquals(MeteorAppearance.ANIMATED_GIF, meteor.appearance());
    }

    private int coloredRowsAt(BufferedImage image, int x) {
        int bg = image.getRGB(0, 0);
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            if (image.getRGB(x, y) != bg) {
                count++;
            }
        }
        return count;
    }

    private int countTrailPixels(BufferedImage image) {
        int count = 0;
        int bg = image.getRGB(0, 0);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != bg) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countTexturedPixels(BufferedImage image) {
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private BufferedImage renderMeteor(double rotation) {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Meteor meteor = new Meteor(
                1,
                0,
                0,
                500,
                15,
                0,
                0,
                -1,
                5,
                rotation,
                0.2,
                MeteorBehavior.STRAIGHT,
                0,
                0,
                0,
                MeteorAppearance.DEFAULT);
        new MeteorRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), meteor, new MeteorShape(5));
        graphics.dispose();
        return image;
    }

    private int differentPixels(BufferedImage first, BufferedImage second) {
        int differences = 0;
        for (int y = 0; y < first.getHeight(); y++) {
            for (int x = 0; x < first.getWidth(); x++) {
                if (first.getRGB(x, y) != second.getRGB(x, y)) {
                    differences++;
                }
            }
        }
        return differences;
    }

    private int countNonBackgroundPixels(BufferedImage image) {
        int background = image.getRGB(0, 0);
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != background) {
                    count++;
                }
            }
        }
        return count;
    }
}
