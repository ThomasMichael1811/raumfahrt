package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.MonitorSide;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WarpEffectRendererTest {

    private static final int WIDTH = 320;
    private static final int HEIGHT = 240;

    @Test
    void intensityBlendetEinUndAus() {
        assertEquals(0.0, WarpEffectRenderer.intensity(0.0), 1e-9);
        assertEquals(1.0, WarpEffectRenderer.intensity(0.5), 1e-9);
        assertEquals(0.0, WarpEffectRenderer.intensity(1.0), 1e-9);
        assertTrue(WarpEffectRenderer.intensity(0.1) > 0.0);
        assertTrue(WarpEffectRenderer.intensity(0.9) > 0.0);
    }

    @Test
    void rechteSeitenscheibeHatFluchtpunktLinksAusserhalb() {
        WarpEffectRenderer.WarpViewport viewport = WarpEffectRenderer.viewportFor(MonitorSide.RIGHT, WIDTH, HEIGHT);

        assertTrue(viewport.vanishingX() < 0.0, "Fluchtpunkt muss links vor dem Monitor liegen");
        assertEquals(0.0, viewport.coreX(), 1e-9);
    }

    @Test
    void linkeSeitenscheibeHatFluchtpunktRechtsAusserhalb() {
        WarpEffectRenderer.WarpViewport viewport = WarpEffectRenderer.viewportFor(MonitorSide.LEFT, WIDTH, HEIGHT);

        assertTrue(viewport.vanishingX() > WIDTH, "Fluchtpunkt muss rechts vor dem Monitor liegen");
        assertEquals((double) WIDTH, viewport.coreX(), 1e-9);
    }

    @Test
    void aktiverWarpZeichnetLichtstreifen() {
        assertTrue(nonBackgroundPixels(render(MonitorSide.RIGHT, 0.5)) > 0, "Warp-Effekt ist unsichtbar");
        assertTrue(nonBackgroundPixels(render(MonitorSide.LEFT, 0.5)) > 0, "Warp-Effekt ist unsichtbar");
    }

    @Test
    void inaktiverWarpLaesstBildUnveraendert() {
        assertEquals(0, nonBackgroundPixels(render(MonitorSide.RIGHT, 0.0)));
    }

    private BufferedImage render(MonitorSide side, double progress) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        new WarpEffectRenderer(new Random(7L)).render(graphics, side, WIDTH, HEIGHT, progress);
        graphics.dispose();
        return image;
    }

    private int nonBackgroundPixels(BufferedImage image) {
        int count = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (luminance(image.getRGB(x, y)) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private int luminance(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return (red + green + blue) / 3;
    }
}
