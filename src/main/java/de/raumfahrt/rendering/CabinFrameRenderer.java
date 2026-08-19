package de.raumfahrt.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

public final class CabinFrameRenderer {

    public static final Color FRAME_COLOR = new Color(0x2B, 0x31, 0x3B);
    public static final int FRAME_THICKNESS = 48;

    public void render(Graphics2D graphics, int width, int height) {
        graphics.setColor(FRAME_COLOR);
        graphics.fillRect(0, 0, width, FRAME_THICKNESS);
        graphics.fillRect(0, height - FRAME_THICKNESS, width, FRAME_THICKNESS);
        graphics.fillRect(0, 0, FRAME_THICKNESS, height);
        graphics.fillRect(width - FRAME_THICKNESS, 0, FRAME_THICKNESS, height);
    }
}
