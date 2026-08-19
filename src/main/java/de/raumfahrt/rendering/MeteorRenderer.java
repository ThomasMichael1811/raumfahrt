package de.raumfahrt.rendering;

import de.raumfahrt.core.Meteor;
import de.raumfahrt.core.MeteorShape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public final class MeteorRenderer {

    public static final Color METEOR_COLOR = new Color(0x6B, 0x5B, 0x45);
    public static final Color METEOR_OUTLINE = new Color(0x2E, 0x26, 0x1C);

    public void render(Graphics2D graphics, Meteor meteor, MeteorShape shape) {
        Polygon polygon = shape.polygon(meteor.size());
        Polygon shifted = new Polygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
        shifted.translate((int) Math.round(meteor.x()), (int) Math.round(meteor.y()));
        graphics.setColor(METEOR_COLOR);
        graphics.fillPolygon(shifted);
        graphics.setColor(METEOR_OUTLINE);
        graphics.drawPolygon(shifted);
    }
}
