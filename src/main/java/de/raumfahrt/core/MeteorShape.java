package de.raumfahrt.core;

import java.awt.Polygon;
import java.util.Random;

public final class MeteorShape {

    public static final int POINT_COUNT = 10;

    private static final double MIN_RADIUS_FACTOR = 0.7;
    private static final double MAX_RADIUS_FACTOR = 1.3;

    private final double[] radiusFactors;

    public MeteorShape(int seed) {
        Random random = new Random(seed);
        radiusFactors = new double[POINT_COUNT];
        for (int i = 0; i < POINT_COUNT; i++) {
            radiusFactors[i] = MIN_RADIUS_FACTOR + random.nextDouble() * (MAX_RADIUS_FACTOR - MIN_RADIUS_FACTOR);
        }
    }

    public Polygon polygon(double size) {
        int[] xs = new int[POINT_COUNT];
        int[] ys = new int[POINT_COUNT];
        for (int i = 0; i < POINT_COUNT; i++) {
            double angle = i * 2.0 * Math.PI / POINT_COUNT;
            double radius = size * radiusFactors[i];
            xs[i] = (int) Math.round(Math.cos(angle) * radius);
            ys[i] = (int) Math.round(Math.sin(angle) * radius);
        }
        return new Polygon(xs, ys, POINT_COUNT);
    }
}
