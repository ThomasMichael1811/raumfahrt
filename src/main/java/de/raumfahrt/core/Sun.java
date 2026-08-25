package de.raumfahrt.core;

public record Sun(double x, double y, double radius, double speedX, SunColor color) {

    public Sun(double x, double y, double radius, double speedX) {
        this(x, y, radius, speedX, SunColor.YELLOW);
    }

    public Sun moved(double deltaSeconds, int width) {
        if (width <= 0) {
            return this;
        }
        double x = this.x - speedX * deltaSeconds;
        while (x < 0) {
            x += width;
        }
        return new Sun(x, y, radius, speedX, color);
    }

    public enum SunColor {
        YELLOW,
        RED
    }
}
