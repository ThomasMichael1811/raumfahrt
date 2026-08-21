package de.raumfahrt.rendering;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MeteorTrail;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public final class MeteorRenderer {

    public static final Color METEOR_COLOR = new Color(0x6B, 0x5B, 0x45);
    public static final Color METEOR_OUTLINE = new Color(0x2E, 0x26, 0x1C);
    public static final Color TRAIL_COLOR = new Color(0x5A, 0x6B, 0x7A);

    static final int PIXEL_SIZE = 6;

    public void render(Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MeteorShape shape) {
        render(graphics, projection, meteor, shape, MonitorView.CENTERED);
    }

    public void render(
            Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MeteorShape shape, MonitorView view) {
        AffineTransform original = graphics.getTransform();
        graphics.translate(
                view.screenX(projection, meteor.x(), meteor.depth()), projection.screenY(meteor.y(), meteor.depth()));
        graphics.rotate(meteor.rotation());
        Polygon polygon = shape.polygon(projection.scale(meteor.size(), meteor.depth()));
        graphics.setColor(METEOR_COLOR);
        graphics.fillPolygon(polygon);
        graphics.setColor(METEOR_OUTLINE);
        graphics.drawPolygon(polygon);
        graphics.setTransform(original);
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
        float size = (float) projection.scale(meteor.size(), meteor.depth());
        for (int i = 0; i < trail.size() - 1; i++) {
            MeteorTrail.TrailPoint from = trail.points().get(i);
            MeteorTrail.TrailPoint to = trail.points().get(i + 1);
            float opacity = (float) trail.opacityAt(i);
            graphics.setColor(new Color(
                    TRAIL_COLOR.getRed() / 255.0f,
                    TRAIL_COLOR.getGreen() / 255.0f,
                    TRAIL_COLOR.getBlue() / 255.0f,
                    opacity));
            drawPixelatedSegment(
                    graphics,
                    view.screenX(projection, from.x(), from.depth()),
                    projection.screenY(from.y(), from.depth()),
                    view.screenX(projection, to.x(), to.depth()),
                    projection.screenY(to.y(), to.depth()),
                    size);
        }
    }

    private void drawPixelatedSegment(
            Graphics2D graphics, double x1, double y1, double x2, double y2, float meteorSize) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.hypot(dx, dy);
        int steps = Math.max(1, (int) (distance / PIXEL_SIZE));
        int blockSize = Math.max(PIXEL_SIZE, (int) (meteorSize * 0.3f));
        for (int step = 0; step <= steps; step++) {
            double t = step / (double) steps;
            int px = (int) Math.round((x1 + dx * t) / PIXEL_SIZE) * PIXEL_SIZE;
            int py = (int) Math.round((y1 + dy * t) / PIXEL_SIZE) * PIXEL_SIZE;
            graphics.fillRect(px, py, blockSize, blockSize);
        }
    }
}
