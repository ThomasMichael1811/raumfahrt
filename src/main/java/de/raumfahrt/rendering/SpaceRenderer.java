package de.raumfahrt.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

public final class SpaceRenderer {

    public static final Color SPACE_BACKGROUND = new Color(0x05, 0x08, 0x12);

    public void render(Graphics2D graphics, int width, int height) {
        graphics.setColor(SPACE_BACKGROUND);
        graphics.fillRect(0, 0, width, height);
    }
}
