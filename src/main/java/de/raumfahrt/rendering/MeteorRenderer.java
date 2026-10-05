package de.raumfahrt.rendering;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorAppearance;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MeteorTrail;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class MeteorRenderer {

    public static final Color METEOR_COLOR = new Color(0x6B, 0x5B, 0x45);
    public static final Color METEOR_OUTLINE = new Color(0x2E, 0x26, 0x1C);
    public static final Color TRAIL_COLOR = new Color(0x5A, 0x6B, 0x7A);

    static final int PIXEL_SIZE = 6;
    private static final float MAX_TRAIL_SPREAD = 24.0f;
    static final AsteroidTextureAtlas ASTEROID_TEXTURES = AsteroidTextureAtlas.load("textures/asteroids.png");
    private static final AnimatedGif ASTEROID_GIF = AnimatedGif.load("gif/asteroid1.gif");
    private static final long GIF_START_TIME = System.currentTimeMillis();

    public void render(Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MeteorShape shape) {
        render(graphics, projection, meteor, shape, MonitorView.CENTERED);
    }

    public void render(
            Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MeteorShape shape, MonitorView view) {
        if (meteor.appearance() == MeteorAppearance.ANIMATED_GIF) {
            renderAnimatedGif(graphics, projection, meteor, view);
            return;
        }
        renderTexturedMeteor(graphics, projection, meteor, view);
    }

    private void renderTexturedMeteor(
            Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MonitorView view) {
        AffineTransform original = graphics.getTransform();
        int diameter = Math.max(1, (int) Math.round(2.0 * projection.scale(meteor.size(), meteor.depth())));
        graphics.translate(
                view.screenX(projection, meteor.x(), meteor.depth()), projection.screenY(meteor.y(), meteor.depth()));
        graphics.rotate(meteor.rotation());
        BufferedImage sprite = ASTEROID_TEXTURES.spriteForSeed(meteor.shapeSeed());
        graphics.drawImage(sprite, -diameter / 2, -diameter / 2, diameter, diameter, null);
        graphics.setTransform(original);
    }

    private void renderAnimatedGif(
            Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MonitorView view) {
        BufferedImage frame = ASTEROID_GIF.frameAt(System.currentTimeMillis() - GIF_START_TIME);
        int diameter = Math.max(1, (int) Math.round(2.0 * projection.scale(meteor.size(), meteor.depth())));
        int x = (int) Math.round(view.screenX(projection, meteor.x(), meteor.depth()) - diameter / 2.0);
        int y = (int) Math.round(projection.screenY(meteor.y(), meteor.depth()) - diameter / 2.0);
        graphics.drawImage(frame, x, y, diameter, diameter, null);
    }

    public void renderTrail(
            Graphics2D graphics,
            MonitorPairProjection projection,
            Meteor meteor,
            MeteorShape shape,
            MeteorTrail trail) {
        renderTrail(graphics, projection, meteor, shape, trail, MonitorView.CENTERED);
    }

    public void renderTrail(
            Graphics2D graphics,
            MonitorPairProjection projection,
            Meteor meteor,
            MeteorShape shape,
            MeteorTrail trail,
            MonitorView view) {
        if (trail == null || trail.size() < 2) {
            return;
        }
        int last = trail.size() - 1;
        for (int i = 0; i < last; i++) {
            MeteorTrail.TrailPoint from = trail.points().get(i);
            MeteorTrail.TrailPoint to = trail.points().get(i + 1);
            float opacity = (float) trail.opacityAt(i);
            graphics.setColor(new Color(
                    TRAIL_COLOR.getRed() / 255.0f,
                    TRAIL_COLOR.getGreen() / 255.0f,
                    TRAIL_COLOR.getBlue() / 255.0f,
                    opacity));
            float spread = MAX_TRAIL_SPREAD * (1.0f - (float) i / last);
            drawSpreadSegment(
                    graphics,
                    view.screenX(projection, from.x(), from.depth()),
                    projection.screenY(from.y(), from.depth()),
                    view.screenX(projection, to.x(), to.depth()),
                    projection.screenY(to.y(), to.depth()),
                    spread);
        }
    }

    private void drawSpreadSegment(Graphics2D graphics, double x1, double y1, double x2, double y2, float spread) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.hypot(dx, dy);
        if (distance < 0.5) {
            drawCross(graphics, x1, y1, spread);
            return;
        }
        double perpX = -dy / distance;
        double perpY = dx / distance;
        int steps = Math.max(1, (int) (distance / PIXEL_SIZE));
        int cross = 1 + (int) (spread / PIXEL_SIZE);
        float spacing = cross > 1 ? spread / (cross - 1) : 0;
        for (int step = 0; step <= steps; step++) {
            double t = step / (double) steps;
            double cx = x1 + dx * t;
            double cy = y1 + dy * t;
            for (int c = 0; c < cross; c++) {
                float offset = cross <= 1 ? 0 : -spacing / 2 + spacing * c;
                int px = (int) Math.round((cx + perpX * offset) / PIXEL_SIZE) * PIXEL_SIZE;
                int py = (int) Math.round((cy + perpY * offset) / PIXEL_SIZE) * PIXEL_SIZE;
                graphics.fillRect(px, py, PIXEL_SIZE, PIXEL_SIZE);
            }
        }
    }

    private void drawCross(Graphics2D graphics, double x, double y, float spread) {
        int cx = (int) Math.round(x / PIXEL_SIZE) * PIXEL_SIZE;
        int cy = (int) Math.round(y / PIXEL_SIZE) * PIXEL_SIZE;
        if (spread < PIXEL_SIZE) {
            graphics.fillRect(cx, cy, PIXEL_SIZE, PIXEL_SIZE);
            return;
        }
        int cross = 1 + (int) (spread / PIXEL_SIZE);
        float spacing = spread / (cross - 1);
        for (int c = 0; c < cross; c++) {
            int px = cx + (int) (-spacing / 2 + spacing * c);
            graphics.fillRect(px, cy, PIXEL_SIZE, PIXEL_SIZE);
        }
    }
}
