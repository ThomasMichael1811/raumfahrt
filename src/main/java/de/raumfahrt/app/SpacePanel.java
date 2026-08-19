package de.raumfahrt.app;

import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.StarField;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public final class SpacePanel extends JPanel {

    private static final int UPDATES_PER_SECOND = 60;

    private final transient SpaceRenderer renderer;
    private final transient StarFieldRenderer starFieldRenderer;
    private final transient MeteorRenderer meteorRenderer;
    private final transient CabinFrameRenderer frameRenderer;
    private final transient StarField starField;
    private final transient MeteorField meteorField;
    private final transient GameLoop gameLoop;
    private transient BufferedImage offscreen;

    public SpacePanel(
            SpaceRenderer renderer,
            StarFieldRenderer starFieldRenderer,
            MeteorRenderer meteorRenderer,
            CabinFrameRenderer frameRenderer,
            StarField starField,
            MeteorField meteorField) {
        this.renderer = renderer;
        this.starFieldRenderer = starFieldRenderer;
        this.meteorRenderer = meteorRenderer;
        this.frameRenderer = frameRenderer;
        this.starField = starField;
        this.meteorField = meteorField;
        this.gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            starField.update(deltaSeconds);
            meteorField.update(deltaSeconds);
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
        for (Meteor meteor : meteorField.meteors()) {
            meteorRenderer.render(target, meteor, new MeteorShape(meteor.shapeSeed()));
        }
        frameRenderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }
}
