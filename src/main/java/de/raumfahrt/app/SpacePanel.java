package de.raumfahrt.app;

import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.StarField;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class SpacePanel extends JPanel {

    private static final int UPDATES_PER_SECOND = 60;

    private final transient SpaceRenderer renderer;
    private final transient StarFieldRenderer starFieldRenderer;
    private final transient CabinFrameRenderer frameRenderer;
    private final transient StarField starField;
    private final transient GameLoop gameLoop;
    private transient BufferedImage offscreen;

    public SpacePanel(SpaceRenderer renderer, StarFieldRenderer starFieldRenderer,
                      CabinFrameRenderer frameRenderer, StarField starField) {
        this.renderer = renderer;
        this.starFieldRenderer = starFieldRenderer;
        this.frameRenderer = frameRenderer;
        this.starField = starField;
        this.gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            starField.update(deltaSeconds);
            repaint();
        });
    }

    public void startGameLoop() {
        gameLoop.start();
    }

    public void stopGameLoop() {
        gameLoop.stop();
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
        starFieldRenderer.render(target, starField.stars(), offscreen.getWidth(), offscreen.getHeight());
        frameRenderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }
}
