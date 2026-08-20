package de.raumfahrt.rendering;

import de.raumfahrt.core.Sun;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public final class SunRenderer {

    public static final Color SUN_CORE = new Color(0xFF, 0xF3, 0x8A);
    public static final Color SUN_EDGE = new Color(0xFF, 0x8A, 0x00, 0x00);

    public void render(Graphics2D graphics, Sun sun) {
        double glow = sun.radius() * 2.5;
        RadialGradientPaint gradient = new RadialGradientPaint(
                new Point2D.Double(sun.x(), sun.y()), (float) glow, new float[] {0.0f, 0.45f, 1.0f}, new Color[] {
                    SUN_CORE, new Color(0xFF, 0xC6, 0x3A), SUN_EDGE
                });
        graphics.setPaint(gradient);
        int diameter = (int) Math.round(glow * 2.0);
        int x = (int) Math.round(sun.x() - glow);
        int y = (int) Math.round(sun.y() - glow);
        graphics.fillOval(x, y, diameter, diameter);
    }
}
