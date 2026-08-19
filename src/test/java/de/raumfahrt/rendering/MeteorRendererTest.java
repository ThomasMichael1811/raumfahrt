package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MeteorRendererTest {

    @Test
    void renderZeichnetFelsbrockenUeberDemHintergrund() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, 100, 100);
        new MeteorRenderer().render(graphics, new Meteor(50, 50, 15, 0, 0, 0, 0.2), new MeteorShape(5));
        graphics.dispose();

        assertTrue(containsRockColor(image));
    }

    @Test
    void rotationVeraendertDarstellung() {
        BufferedImage first = renderMeteor(0.0);
        BufferedImage second = renderMeteor(Math.PI);

        assertTrue(!rockPositions(first).equals(rockPositions(second)));
    }

    private Set<String> rockPositions(BufferedImage image) {
        Set<String> result = new HashSet<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == MeteorRenderer.METEOR_COLOR.getRGB()) {
                    result.add(x + "," + y);
                }
            }
        }
        return result;
    }

    private BufferedImage renderMeteor(double rotation) {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        new SpaceRenderer().render(graphics, 100, 100);
        new MeteorRenderer().render(graphics, new Meteor(50, 50, 15, 0, 0, rotation, 0.2), new MeteorShape(5));
        graphics.dispose();
        return image;
    }

    private boolean containsRockColor(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == MeteorRenderer.METEOR_COLOR.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }
}
