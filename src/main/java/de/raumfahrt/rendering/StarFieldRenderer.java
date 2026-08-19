package de.raumfahrt.rendering;

import de.raumfahrt.core.Star;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public final class StarFieldRenderer {

    public void render(Graphics2D graphics, List<Star> stars, int width, int height) {
        for (Star star : stars) {
            int brightness = (int) Math.round(star.brightness() * 255);
            graphics.setColor(new Color(brightness, brightness, brightness));
            int diameter = (int) Math.round(star.size() * 2.0);
            int x = (int) Math.round(star.x() - star.size());
            int y = (int) Math.round(star.y() - star.size());
            graphics.fillOval(x, y, diameter, diameter);
        }
    }
}
