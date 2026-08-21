package de.raumfahrt.rendering;

import de.raumfahrt.core.Explosion;
import de.raumfahrt.core.ExplosionFragment;
import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public final class ExplosionRenderer {

    public static final Color EXPLOSION_COLOR = new Color(0xFF, 0xA5, 0x00);
    public static final Color EXPLOSION_OUTLINE = new Color(0x8B, 0x45, 0x00);

    public void render(Graphics2D graphics, MonitorPairProjection projection, Explosion explosion) {
        render(graphics, projection, explosion, MonitorView.CENTERED);
    }

    public void render(Graphics2D graphics, MonitorPairProjection projection, Explosion explosion, MonitorView view) {
        float opacity = (float) explosion.opacity();
        Color fill = new Color(
                EXPLOSION_COLOR.getRed(), EXPLOSION_COLOR.getGreen(), EXPLOSION_COLOR.getBlue(), (int) (opacity * 255));
        Color outline =
                new Color(EXPLOSION_OUTLINE.getRed(), EXPLOSION_OUTLINE.getGreen(), EXPLOSION_OUTLINE.getBlue(), (int)
                        (opacity * 255));
        for (ExplosionFragment fragment : explosion.fragments()) {
            int radius = (int) projection.scale(fragment.size(), fragment.depth());
            Polygon polygon = new Polygon();
            for (int i = 0; i < 6; i++) {
                double angle = fragment.rotation() + i * Math.PI / 3.0;
                polygon.addPoint(
                        (int) (view.screenX(projection, fragment.x(), fragment.depth()) + Math.cos(angle) * radius),
                        (int) (projection.screenY(fragment.y(), fragment.depth()) + Math.sin(angle) * radius));
            }
            graphics.setColor(fill);
            graphics.fillPolygon(polygon);
            graphics.setColor(outline);
            graphics.drawPolygon(polygon);
        }
    }
}
