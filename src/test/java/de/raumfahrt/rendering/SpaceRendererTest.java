package de.raumfahrt.rendering;

import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaceRendererTest {

    @Test
    void renderFuelltFlaecheMitWeltraumhintergrund() {
        BufferedImage image = new BufferedImage(40, 30, BufferedImage.TYPE_INT_RGB);
        SpaceRenderer renderer = new SpaceRenderer();

        Graphics2D graphics = image.createGraphics();
        renderer.render(graphics, 40, 30);
        graphics.dispose();

        assertEquals(SpaceRenderer.SPACE_BACKGROUND.getRGB(), image.getRGB(0, 0));
        assertEquals(SpaceRenderer.SPACE_BACKGROUND.getRGB(), image.getRGB(39, 0));
        assertEquals(SpaceRenderer.SPACE_BACKGROUND.getRGB(), image.getRGB(0, 29));
        assertEquals(SpaceRenderer.SPACE_BACKGROUND.getRGB(), image.getRGB(20, 15));
    }
}
