package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Explosion;
import de.raumfahrt.core.ExplosionFragment;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExplosionRendererTest {

    private static final int W = 100;
    private static final int H = 100;

    @Test
    void renderZeichnetFragmenteUeberDemHintergrund() {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, W, H);
        Explosion explosion =
                new Explosion(0, 0, 100, 0.0, 1.2, List.of(new ExplosionFragment(0, 0, 100, 20, 10, 0, 0.0, 0.1)));
        new ExplosionRenderer().render(graphics, new MonitorPairProjection(W, H, 0, 400), explosion);
        graphics.dispose();

        assertTrue(countExplosionPixels(image) > 0);
    }

    private int countExplosionPixels(BufferedImage image) {
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == ExplosionRenderer.EXPLOSION_COLOR.getRGB()) {
                    count++;
                }
            }
        }
        return count;
    }
}
