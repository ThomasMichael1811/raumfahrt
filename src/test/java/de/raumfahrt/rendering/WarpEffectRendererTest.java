package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WarpEffectRendererTest {

    private static final int SIZE = 240;

    @Test
    void intensityBlendetEinUndAus() {
        assertEquals(0.0, WarpEffectRenderer.intensity(0.0), 1e-9);
        assertEquals(1.0, WarpEffectRenderer.intensity(0.5), 1e-9);
        assertEquals(0.0, WarpEffectRenderer.intensity(1.0), 1e-9);
        assertTrue(WarpEffectRenderer.intensity(0.1) > 0.0);
        assertTrue(WarpEffectRenderer.intensity(0.9) > 0.0);
    }

    @Test
    void aktiverWarpZeichnetLichtstreifen() {
        BufferedImage image = render(0.5);

        assertTrue(nonBackgroundPixels(image) > 0, "Warp-Effekt ist unsichtbar");
    }

    @Test
    void inaktiverWarpLaesstBildUnveraendert() {
        BufferedImage image = render(0.0);

        assertEquals(0, nonBackgroundPixels(image));
    }

    @Test
    void lichtkernZeichnetImZentrum() {
        BufferedImage image = render(0.5);

        assertTrue(luminance(image.getRGB(SIZE / 2, SIZE / 2)) > 0);
    }

    private BufferedImage render(double progress) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, SIZE, SIZE);
        new WarpEffectRenderer(new Random(7L)).render(graphics, SIZE / 2.0, SIZE / 2.0, SIZE, SIZE, progress);
        graphics.dispose();
        return image;
    }

    private int nonBackgroundPixels(BufferedImage image) {
        int count = 0;
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
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
