package de.raumfahrt.rendering;

import de.raumfahrt.core.Star;
import de.raumfahrt.core.WarpState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public final class StarFieldRenderer {

    private static final double STREAK_FACTOR = 0.8;

    public void render(Graphics2D graphics, List<Star> stars, int width, int height) {
        render(graphics, stars, width, height, new WarpState());
    }

    public void render(Graphics2D graphics, List<Star> stars, int width, int height, WarpState warp) {
        for (Star star : stars) {
            int brightness = (int) Math.round(star.brightness() * 255);
            graphics.setColor(new Color(brightness, brightness, brightness));
            int x = (int) Math.round(star.x());
            int y = (int) Math.round(star.y());
            if (warp.active()) {
                int streakLength = Math.max(2, (int) Math.round(warp.speed() * STREAK_FACTOR));
                graphics.drawLine(x, y, x + streakLength, y);
            } else {
                int diameter = (int) Math.round(star.size() * 2.0);
                int px = (int) Math.round(star.x() - star.size());
                int py = (int) Math.round(star.y() - star.size());
                graphics.fillOval(px, py, diameter, diameter);
            }
        }
    }
}
