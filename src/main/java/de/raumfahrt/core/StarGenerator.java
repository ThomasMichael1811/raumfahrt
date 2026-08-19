package de.raumfahrt.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class StarGenerator {

    public static final int LAYER_COUNT = 3;

    private static final int[] STARS_PER_LAYER = {180, 120, 60};
    private static final double[] SIZE_MIN = {0.8, 1.4, 2.5};
    private static final double[] SIZE_MAX = {1.2, 2.2, 4.0};
    private static final double[] BRIGHTNESS_MIN = {0.25, 0.5, 0.85};
    private static final double[] BRIGHTNESS_MAX = {0.45, 0.75, 1.0};

    public List<Star> generate(int width, int height, Random random) {
        List<Star> stars = new ArrayList<>();
        for (int depth = 0; depth < LAYER_COUNT; depth++) {
            for (int i = 0; i < STARS_PER_LAYER[depth]; i++) {
                stars.add(new Star(
                    random.nextDouble() * width,
                    random.nextDouble() * height,
                    randomRange(random, SIZE_MIN[depth], SIZE_MAX[depth]),
                    randomRange(random, BRIGHTNESS_MIN[depth], BRIGHTNESS_MAX[depth]),
                    depth));
            }
        }
        return stars;
    }

    private double randomRange(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }
}
