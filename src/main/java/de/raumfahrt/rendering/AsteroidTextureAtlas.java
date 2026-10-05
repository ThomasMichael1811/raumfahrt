package de.raumfahrt.rendering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

final class AsteroidTextureAtlas {

    static final int SPRITE_COUNT = 5;
    private static final int TOP_ROW_HEIGHT = 320;

    private final List<BufferedImage> sprites;

    private AsteroidTextureAtlas(List<BufferedImage> sprites) {
        this.sprites = List.copyOf(sprites);
    }

    static AsteroidTextureAtlas load(String resourcePath) {
        try (InputStream input = AsteroidTextureAtlas.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException("Textur-Ressource nicht gefunden: " + resourcePath);
            }
            BufferedImage atlas = ImageIO.read(input);
            if (atlas == null) {
                throw new IllegalStateException("Textur-Ressource enthält kein lesbares Bild: " + resourcePath);
            }
            return fromAtlas(atlas, resourcePath);
        } catch (IOException exception) {
            throw new IllegalStateException("Textur-Ressource konnte nicht geladen werden: " + resourcePath, exception);
        }
    }

    BufferedImage spriteForSeed(int seed) {
        return sprites.get(Math.floorMod(seed, sprites.size()));
    }

    BufferedImage sprite(int index) {
        return sprites.get(index);
    }

    private static AsteroidTextureAtlas fromAtlas(BufferedImage atlas, String resourcePath) {
        if (atlas.getWidth() < SPRITE_COUNT || atlas.getHeight() < TOP_ROW_HEIGHT) {
            throw new IllegalStateException("Textur-Atlas zu klein: " + resourcePath);
        }
        int rowHeight = TOP_ROW_HEIGHT;
        List<BufferedImage> sprites = new ArrayList<>(SPRITE_COUNT);
        for (int index = 0; index < SPRITE_COUNT; index++) {
            int left = index * atlas.getWidth() / SPRITE_COUNT;
            int right = (index + 1) * atlas.getWidth() / SPRITE_COUNT;
            sprites.add(atlas.getSubimage(left, 0, right - left, rowHeight));
        }
        return new AsteroidTextureAtlas(sprites);
    }
}
