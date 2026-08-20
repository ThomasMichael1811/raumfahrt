package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectionRendererTest {

    private static final int W = 400;
    private static final int H = 300;

    @Test
    void rendertObjektAufLinkenUndRechtenMonitor() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 20.0, 200.0);
        ProjectedObject left = new ProjectedObject(-300.0, 0.0, 500.0, 8.0, Color.WHITE);
        ProjectedObject right = new ProjectedObject(300.0, 0.0, 500.0, 8.0, Color.WHITE);

        BufferedImage image = new ProjectionRenderer().render(projection, List.of(left, right));

        assertTrue(hasColor(image, Color.WHITE, 0, W));
        assertTrue(hasColor(image, Color.WHITE, W + 20, 2 * W + 20));
    }

    @Test
    void lueckenBereichBleibtLeer() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 20.0, 200.0);
        ProjectedObject center = new ProjectedObject(0.0, 0.0, 500.0, 8.0, Color.WHITE);

        BufferedImage image = new ProjectionRenderer().render(projection, List.of(center));

        for (int x = W; x < W + 20; x++) {
            for (int y = 0; y < H; y++) {
                assertEquals(ProjectionRenderer.GAP_BACKGROUND.getRGB(), image.getRGB(x, y));
            }
        }
    }

    @Test
    void entferntesObjektWandertWenigerAlsNahes() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 20.0, 200.0);
        ProjectedObject near = new ProjectedObject(200.0, 0.0, 200.0, 8.0, Color.WHITE);
        ProjectedObject far = new ProjectedObject(200.0, 0.0, 2000.0, 8.0, Color.WHITE);

        BufferedImage nearImage = new ProjectionRenderer().render(projection, List.of(near));
        BufferedImage farImage = new ProjectionRenderer().render(projection, List.of(far));

        int nearX = leftmostX(nearImage, Color.WHITE);
        int farX = leftmostX(farImage, Color.WHITE);
        assertTrue(Math.abs(farX - W) < Math.abs(nearX - W));
    }

    @Test
    void rendertSzeneMitMehrerenTiefenAufBeidenMonitoren() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 20.0, 200.0);
        List<ProjectedObject> scene = List.of(
                new ProjectedObject(-300.0, 40.0, 300.0, 6.0, Color.WHITE),
                new ProjectedObject(-150.0, -30.0, 600.0, 5.0, Color.WHITE),
                new ProjectedObject(150.0, 20.0, 400.0, 9.0, new Color(0xFF, 0x8A, 0x00)),
                new ProjectedObject(320.0, -10.0, 800.0, 4.0, Color.WHITE));

        BufferedImage image = new ProjectionRenderer().render(projection, scene);

        assertTrue(hasNonBackground(image, 0, W));
        assertTrue(hasNonBackground(image, W + 20, 2 * W + 20));
    }

    private int leftmostX(BufferedImage image, Color color) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == color.getRGB()) {
                    return x;
                }
            }
        }
        return -1;
    }

    private boolean hasColor(BufferedImage image, Color color, int xFrom, int xTo) {
        for (int x = xFrom; x < xTo; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == color.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasNonBackground(BufferedImage image, int xFrom, int xTo) {
        for (int x = xFrom; x < xTo; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                if (rgb != ProjectionRenderer.SPACE_BACKGROUND.getRGB()
                        && rgb != ProjectionRenderer.GAP_BACKGROUND.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }
}
