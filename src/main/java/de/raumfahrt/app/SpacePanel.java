package de.raumfahrt.app;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MonitorPairProjection;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import de.raumfahrt.rendering.SunRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public final class SpacePanel extends JPanel {

    private static final int FOCAL_PX = 400;
    private static final double GAP_PX = 0.0;

    private final transient SpaceRenderer renderer;
    private final transient StarFieldRenderer starFieldRenderer;
    private final transient MeteorRenderer meteorRenderer;
    private final transient CabinFrameRenderer frameRenderer;
    private final transient SunRenderer sunRenderer = new SunRenderer();
    private final transient SimulationWorld world;
    private transient BufferedImage offscreen;

    public SpacePanel(
            SpaceRenderer renderer,
            StarFieldRenderer starFieldRenderer,
            MeteorRenderer meteorRenderer,
            CabinFrameRenderer frameRenderer,
            SimulationWorld world) {
        this.renderer = renderer;
        this.starFieldRenderer = starFieldRenderer;
        this.meteorRenderer = meteorRenderer;
        this.frameRenderer = frameRenderer;
        this.world = world;
    }

    public SimulationWorld world() {
        return world;
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
        MonitorPairProjection projection =
                new MonitorPairProjection(offscreen.getWidth(), offscreen.getHeight(), GAP_PX, FOCAL_PX);
        renderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.translate(-world.cameraX(), 0);
        sunRenderer.render(target, world.sun());
        starFieldRenderer.render(target, world.stars(), offscreen.getWidth(), offscreen.getHeight());
        for (Meteor meteor : world.meteors()) {
            meteorRenderer.render(target, projection, meteor, new MeteorShape(meteor.shapeSeed()));
        }
        target.translate(world.cameraX(), 0);
        frameRenderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }
}
