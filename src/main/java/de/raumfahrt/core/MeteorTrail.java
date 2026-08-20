package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;

public final class MeteorTrail {

    public record TrailPoint(double x, double y, double depth) {}

    private static final double HEAD_OPACITY = 0.35;

    private final double maxLength;
    private final List<TrailPoint> points = new ArrayList<>();

    public MeteorTrail(double maxLength) {
        this.maxLength = maxLength;
    }

    public void push(TrailPoint point, double segmentLength) {
        points.add(point);
        double total = 0.0;
        for (int i = points.size() - 1; i > 0; i--) {
            TrailPoint a = points.get(i);
            TrailPoint b = points.get(i - 1);
            total += Math.hypot(a.x() - b.x(), Math.hypot(a.y() - b.y(), a.depth() - b.depth()));
        }
        while (total > maxLength && points.size() > 1) {
            points.remove(0);
            total = 0.0;
            for (int i = points.size() - 1; i > 0; i--) {
                TrailPoint a = points.get(i);
                TrailPoint b = points.get(i - 1);
                total += Math.hypot(a.x() - b.x(), Math.hypot(a.y() - b.y(), a.depth() - b.depth()));
            }
        }
    }

    public List<TrailPoint> points() {
        return List.copyOf(points);
    }

    public int size() {
        return points.size();
    }

    public double opacityAt(int indexFromHead) {
        int size = points.size();
        if (size == 0) {
            return 0.0;
        }
        double t = indexFromHead / (double) size;
        return HEAD_OPACITY * (1.0 - t);
    }

    public double headOpacity() {
        return HEAD_OPACITY;
    }
}
