package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Star;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.Test;

class StarFieldRendererTest {

    @Test
    void renderZeichnetSterneUeberDemHintergrund() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        StarFieldRenderer renderer = new StarFieldRenderer();

        Graphics2D graphics = image.createGraphics();
        renderer.render(graphics, List.of(new Star(20, 20, 2.0, 1.0, 2)), 100, 100);
        graphics.dispose();

        assertTrue(luminance(image.getRGB(20, 20)) > luminance(SpaceRenderer.SPACE_BACKGROUND.getRGB()));
    }

    @Test
    void dunklerSternIstWenigerHellAlsHellerStern() {
        BufferedImage dim = renderStar(0.25);
        BufferedImage bright = renderStar(1.0);

        assertTrue(luminance(bright.getRGB(50, 50)) > luminance(dim.getRGB(50, 50)));
    }

    private BufferedImage renderStar(double brightness) {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new StarFieldRenderer().render(graphics, List.of(new Star(50, 50, 3.0, brightness, 2)), 100, 100);
        graphics.dispose();
        return image;
    }

    private int luminance(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return (red + green + blue) / 3;
    }
}
