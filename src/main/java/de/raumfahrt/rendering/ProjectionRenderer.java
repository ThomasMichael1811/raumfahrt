package de.raumfahrt.rendering;

import de.raumfahrt.core.MonitorPairProjection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public final class ProjectionRenderer {

    public static final Color SPACE_BACKGROUND = new Color(0x05, 0x08, 0x14);
    public static final Color GAP_BACKGROUND = new Color(0x00, 0x00, 0x00);

    public BufferedImage render(MonitorPairProjection projection, List<ProjectedObject> objects) {
        int width = projection.monitorWidthPx();
        int height = projection.monitorHeightPx();
        BufferedImage image = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(SPACE_BACKGROUND);
        graphics.fillRect(0, 0, width * 2, height);
        int gap = (int) Math.round(projection.gapPx());
        graphics.setColor(GAP_BACKGROUND);
        graphics.fillRect(width - gap / 2, 0, gap, height);
        for (ProjectedObject object : objects) {
            renderObject(graphics, projection, object, width);
        }
        graphics.dispose();
        return image;
    }

    private void renderObject(
            Graphics2D graphics, MonitorPairProjection projection, ProjectedObject object, int width) {
        double leftX = projection.screenXLeft(object.worldX(), object.depth());
        double rightX = projection.screenXRight(object.worldX(), object.depth());
        double y = projection.screenY(object.worldY(), object.depth());
        graphics.setColor(object.color());
        if (leftX >= 0 && leftX < width) {
            fillDisc(graphics, leftX, y, object.radiusPx());
        }
        if (rightX >= width && rightX < width * 2) {
            fillDisc(graphics, rightX, y, object.radiusPx());
        }
    }

    private void fillDisc(Graphics2D graphics, double x, double y, double radius) {
        int diameter = (int) Math.round(radius * 2.0);
        int cx = (int) Math.round(x);
        int cy = (int) Math.round(y);
        graphics.fillOval(cx - diameter / 2, cy - diameter / 2, diameter, diameter);
    }
}
