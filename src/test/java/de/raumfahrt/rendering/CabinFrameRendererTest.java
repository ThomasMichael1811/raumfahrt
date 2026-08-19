package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class CabinFrameRendererTest {

    @Test
    void renderZeichnetRahmenAnAllenVierSeiten() {
        BufferedImage image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
        CabinFrameRenderer renderer = new CabinFrameRenderer();

        Graphics2D graphics = image.createGraphics();
        renderer.render(graphics, 200, 150);
        graphics.dispose();

        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(0, 0));
        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(199, 0));
        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(0, 149));
        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(199, 149));
        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(100, 0));
        assertEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(0, 75));
    }

    @Test
    void renderLaesstSichtMitteFrei() {
        BufferedImage image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
        CabinFrameRenderer renderer = new CabinFrameRenderer();

        Graphics2D graphics = image.createGraphics();
        renderer.render(graphics, 200, 150);
        graphics.dispose();

        assertNotEquals(CabinFrameRenderer.FRAME_COLOR.getRGB(), image.getRGB(100, 75));
    }
}
