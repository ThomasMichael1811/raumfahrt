package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class AsteroidTextureAtlasTest {

    @Test
    void laedtFuenfObereSpritesMitTransparenz() {
        AsteroidTextureAtlas atlas = AsteroidTextureAtlas.load("textures/asteroids.png");

        assertEquals(5, AsteroidTextureAtlas.SPRITE_COUNT);
        for (int index = 0; index < AsteroidTextureAtlas.SPRITE_COUNT; index++) {
            BufferedImage sprite = atlas.sprite(index);
            assertEquals(320, sprite.getHeight());
            assertTrue(hasTransparentPixel(sprite));
        }
    }

    @Test
    void spriteAuswahlIstDeterministischUndWiederverwendetSprites() {
        AsteroidTextureAtlas atlas = AsteroidTextureAtlas.load("textures/asteroids.png");

        assertSame(atlas.spriteForSeed(0), atlas.spriteForSeed(5));
        assertSame(atlas.spriteForSeed(-1), atlas.sprite(4));
        assertNotSame(atlas.spriteForSeed(0), atlas.spriteForSeed(1));
    }

    private boolean hasTransparentPixel(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
