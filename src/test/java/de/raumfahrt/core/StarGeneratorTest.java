package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarGeneratorTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Test
    void generateBelegtMindestensDreiEbenen() {
        List<Star> stars = new StarGenerator().generate(WIDTH, HEIGHT, new Random(42L));

        assertEquals(360, stars.size());
        assertEquals(180, stars.stream().filter(s -> s.depth() == 0).count());
        assertEquals(120, stars.stream().filter(s -> s.depth() == 1).count());
        assertEquals(60, stars.stream().filter(s -> s.depth() == 2).count());
    }

    @Test
    void gleicherSeedErzeugtGleicheSterne() {
        List<Star> first = new StarGenerator().generate(WIDTH, HEIGHT, new Random(7L));
        List<Star> second = new StarGenerator().generate(WIDTH, HEIGHT, new Random(7L));

        assertEquals(first, second);
    }

    @Test
    void naheSterneSindGroesserUndHellerAlsFerne() {
        List<Star> stars = new StarGenerator().generate(WIDTH, HEIGHT, new Random(1L));

        double maxFarSize = stars.stream().filter(s -> s.depth() == 0).mapToDouble(Star::size).max().orElse(0);
        double minNearSize = stars.stream().filter(s -> s.depth() == 2).mapToDouble(Star::size).min().orElse(0);
        double maxFarBrightness = stars.stream().filter(s -> s.depth() == 0)
            .mapToDouble(Star::brightness).max().orElse(0);
        double minNearBrightness = stars.stream().filter(s -> s.depth() == 2)
            .mapToDouble(Star::brightness).min().orElse(0);

        assertTrue(minNearSize > maxFarSize);
        assertTrue(minNearBrightness > maxFarBrightness);
    }

    @Test
    void positionenLiegenImFensterUndSindEindeutig() {
        List<Star> stars = new StarGenerator().generate(WIDTH, HEIGHT, new Random(3L));

        for (Star star : stars) {
            assertTrue(star.x() >= 0 && star.x() < WIDTH);
            assertTrue(star.y() >= 0 && star.y() < HEIGHT);
        }
        long uniquePositions = stars.stream().map(s -> s.x() + "," + s.y()).distinct().count();
        assertEquals(stars.size(), uniquePositions);
    }
}
