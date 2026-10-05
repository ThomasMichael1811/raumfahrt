package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class AnimatedGifTest {

    @Test
    void asteroidGifLiefert60FramesMit60MillisekundenUndEndlosschleife() {
        AnimatedGif gif = AnimatedGif.load("gif/asteroid1.gif");

        assertEquals(60, gif.frameCount());
        for (int index = 0; index < gif.frameCount(); index++) {
            assertEquals(60, gif.frameDurationMillis(index));
        }
        assertTrue(differentPixels(gif.frameAt(0), gif.frameAt(60)) > 0);
        assertSame(gif.frameAt(0), gif.frameAt(3600));
    }

    @Test
    void asteroidGifErhaeltTransparentePixel() {
        BufferedImage frame = AnimatedGif.load("gif/asteroid1.gif").frameAt(0);
        boolean hasTransparentPixel = false;
        for (int y = 0; y < frame.getHeight() && !hasTransparentPixel; y++) {
            for (int x = 0; x < frame.getWidth(); x++) {
                if ((frame.getRGB(x, y) >>> 24) == 0) {
                    hasTransparentPixel = true;
                    break;
                }
            }
        }

        assertTrue(hasTransparentPixel);
    }

    @Test
    void zweitesAsteroidGifErhaeltTransparentePixel() {
        BufferedImage frame =
                AnimatedGif.loadWithTransparentBlack("gif/asteroid2.gif").frameAt(0);

        boolean hasTransparentPixel = false;
        for (int y = 0; y < frame.getHeight() && !hasTransparentPixel; y++) {
            for (int x = 0; x < frame.getWidth(); x++) {
                if ((frame.getRGB(x, y) >>> 24) == 0) {
                    hasTransparentPixel = true;
                    break;
                }
            }
        }

        assertTrue(hasTransparentPixel);
    }

    @Test
    void zweitesAsteroidGifWirdGeladenUndAnimiert() {
        AnimatedGif gif = AnimatedGif.load("gif/asteroid2.gif");

        assertTrue(gif.frameCount() > 1);
        assertTrue(gif.frameDurationMillis(0) > 0);
        assertTrue(differentPixels(gif.frameAt(0), gif.frameAt(gif.frameDurationMillis(0))) > 0);
        assertEquals(0, differentPixels(gif.frameAt(0), gif.frameAt(gif.frameDurationMillis(0) * gif.frameCount())));
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
}
