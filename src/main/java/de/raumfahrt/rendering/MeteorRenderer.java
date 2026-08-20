package de.raumfahrt.rendering;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MeteorTrail;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public final class MeteorRenderer {

    public static final Color METEOR_COLOR = new Color(0x6B, 0x5B, 0x45);
    public static final Color METEOR_OUTLINE = new Color(0x2E, 0x26, 0x1C);
    public static final Color TRAIL_COLOR = new Color(0x5A, 0x6B, 0x7A);

    public void render(Graphics2D graphics, MonitorPairProjection projection, Meteor meteor, MeteorShape shape) {
        AffineTransform original = graphics.getTransform();
        graphics.translate(
                projection.screenXCentered(meteor.x(), meteor.depth()), projection.screenY(meteor.y(), meteor.depth()));
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
            graphics.setStroke(new BasicStroke(Math.max(1.0f, size * 0.1f)));
            graphics.drawLine(
                    (int) projection.screenXCentered(from.x(), from.depth()),
                    (int) projection.screenY(from.y(), from.depth()),
                    (int) projection.screenXCentered(to.x(), to.depth()),
                    (int) projection.screenY(to.y(), to.depth()));
        }
    }
}
