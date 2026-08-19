package de.raumfahrt.app;

import de.raumfahrt.rendering.SpaceRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class SpacePanel extends JPanel {

    private final transient SpaceRenderer renderer;
    private transient BufferedImage offscreen;

    public SpacePanel(SpaceRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int width = getWidth();
        int height = getHeight();
        if (offscreen == null || offscreen.getWidth() != width || offscreen.getHeight() != height) {
            offscreen = new BufferedImage(Math.max(width, 1), Math.max(height, 1), BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D target = offscreen.createGraphics();
        renderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }
}
