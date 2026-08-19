package de.raumfahrt.app;

import de.raumfahrt.core.Star;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class SpacePanel extends JPanel {

    private final transient SpaceRenderer renderer;
    private final transient StarFieldRenderer starFieldRenderer;
    private final transient CabinFrameRenderer frameRenderer;
    private final transient List<Star> stars;
    private transient BufferedImage offscreen;

    public SpacePanel(SpaceRenderer renderer, StarFieldRenderer starFieldRenderer,
                      CabinFrameRenderer frameRenderer, List<Star> stars) {
        this.renderer = renderer;
        this.starFieldRenderer = starFieldRenderer;
        this.frameRenderer = frameRenderer;
        this.stars = new ArrayList<>(stars);
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
        starFieldRenderer.render(target, stars, offscreen.getWidth(), offscreen.getHeight());
        frameRenderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }
}


