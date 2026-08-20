package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Sun;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class SunRendererTest {

    @Test
    void renderZeichnetSonnenscheibeMitKern() {
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SunRenderer().render(graphics, new Sun(200, 200, 50, 5.0));
        graphics.dispose();

        assertEquals(SunRenderer.SUN_CORE.getRGB(), image.getRGB(200, 200));
    }

    @Test
    void renderErzeugtGlowUmDenKern() {
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SunRenderer().render(graphics, new Sun(200, 200, 50, 5.0));
        graphics.dispose();

        assertTrue(glowPresent(image, 200, 200));
    }

    private boolean glowPresent(BufferedImage image, int centerX, int centerY) {
        for (int x = centerX - 60; x <= centerX + 60; x++) {
            for (int y = centerY - 60; y <= centerY + 60; y++) {
                int red = (image.getRGB(x, y) >> 16) & 0xFF;
                int green = (image.getRGB(x, y) >> 8) & 0xFF;
                if (red > 200 && green > 100 && x != centerX) {
                    return true;
                }
            }
        }
        return false;
    }
}
