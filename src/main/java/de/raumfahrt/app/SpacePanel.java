package de.raumfahrt.app;

import de.raumfahrt.core.Explosion;
import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MonitorPairProjection;
import de.raumfahrt.core.SceneType;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.core.Sun;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.ExplosionRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.MonitorView;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import de.raumfahrt.rendering.SunRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public final class SpacePanel extends JPanel {

    private static final double GAP_PX = 0.0;
    static final double DEFAULT_FOCAL_PX = 400.0;

    private final transient SpaceRenderer renderer;
    private final transient StarFieldRenderer starFieldRenderer;
    private final transient MeteorRenderer meteorRenderer;
    private final transient CabinFrameRenderer frameRenderer;
    private final transient ExplosionRenderer explosionRenderer = new ExplosionRenderer();
    private final transient SunRenderer sunRenderer = new SunRenderer();
    private final transient SimulationWorld world;
    private final transient MonitorView view;
    private transient double focalPx = DEFAULT_FOCAL_PX;
    private transient BufferedImage offscreen;

    public void setFocalPx(double focalPx) {
        if (focalPx <= 0) {
            throw new IllegalArgumentException("Focal muss positiv sein: " + focalPx);
        }
        this.focalPx = focalPx;
    }

    public SpacePanel(
            SpaceRenderer renderer,
            StarFieldRenderer starFieldRenderer,
            MeteorRenderer meteorRenderer,
            CabinFrameRenderer frameRenderer,
            SimulationWorld world) {
        this(renderer, starFieldRenderer, meteorRenderer, frameRenderer, world, MonitorView.CENTERED);
    }

    public SpacePanel(
            SpaceRenderer renderer,
            StarFieldRenderer starFieldRenderer,
            MeteorRenderer meteorRenderer,
            CabinFrameRenderer frameRenderer,
            SimulationWorld world,
            MonitorView view) {
        this.renderer = renderer;
        this.starFieldRenderer = starFieldRenderer;
        this.meteorRenderer = meteorRenderer;
        this.frameRenderer = frameRenderer;
        this.world = world;
        this.view = view;
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
                new MonitorPairProjection(offscreen.getWidth(), offscreen.getHeight(), GAP_PX, focalPx);
        renderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.translate(-world.cameraX(), 0);
        boolean warpActive = world.warpState().active();
        if (!warpActive) {
            renderSun(target);
        }
        starFieldRenderer.render(target, world.stars(), offscreen.getWidth(), offscreen.getHeight(), world.warpState());
        if (!warpActive) {
            renderMeteors(target, projection, view);
            renderExplosions(target, projection, view);
        }
        target.translate(world.cameraX(), 0);
        frameRenderer.render(target, offscreen.getWidth(), offscreen.getHeight());
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }

    private void renderMeteors(Graphics2D target, MonitorPairProjection projection, MonitorView view) {
        for (Meteor meteor : world.meteors()) {
            MeteorShape shape = new MeteorShape(meteor.shapeSeed());
            meteorRenderer.renderTrail(target, projection, meteor, shape, world.trailFor(meteor.id()), view);
            meteorRenderer.render(target, projection, meteor, shape, view);
        }
    }

    private void renderExplosions(Graphics2D target, MonitorPairProjection projection, MonitorView view) {
        for (Explosion explosion : world.explosions()) {
            explosionRenderer.render(target, projection, explosion, view);
        }
    }

    private void renderSun(Graphics2D target) {
        Sun sun = world.sun();
        SceneType scene = world.scene();
        switch (scene) {
            case NO_SUN -> {
                return;
            }
            case RED_SUN -> sunRenderer.render(target, sun, Sun.SunColor.RED);
            case TWO_SUNS -> {
                sunRenderer.render(target, sun, Sun.SunColor.YELLOW);
                Sun second = new Sun(world.width() * 0.85, sun.y() * 0.7, sun.radius(), sun.speedX());
                sunRenderer.render(target, second, Sun.SunColor.YELLOW);
            }
            default -> sunRenderer.render(target, sun, Sun.SunColor.YELLOW);
        }
    }
}
