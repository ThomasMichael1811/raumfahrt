package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class StarField {

    private static final double[] LAYER_SPEEDS = {20.0, 45.0, 90.0};

    private final int width;
    private final List<Star> stars;

    public StarField(int width, List<Star> stars) {
        this.width = width;
        this.stars = new ArrayList<>(stars);
    }

    public List<Star> stars() {
        return List.copyOf(stars);
    }

    public void update(double deltaSeconds) {
        for (int i = 0; i < stars.size(); i++) {
            stars.set(i, moved(stars.get(i), deltaSeconds));
        }
    }

    private Star moved(Star star, double deltaSeconds) {
        double distance = LAYER_SPEEDS[star.depth()] * deltaSeconds;
        double x = star.x() - distance;
        while (x < 0) {
            x += width;
        }
        return new Star(x, star.y(), star.size(), star.brightness(), star.depth());
    }
}
