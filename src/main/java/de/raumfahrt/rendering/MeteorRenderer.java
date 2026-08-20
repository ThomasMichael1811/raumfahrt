package de.raumfahrt.rendering;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public final class MeteorRenderer {

    public static final Color METEOR_COLOR = new Color(0x6B, 0x5B, 0x45);
    public static final Color METEOR_OUTLINE = new Color(0x2E, 0x26, 0x1C);

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
}
