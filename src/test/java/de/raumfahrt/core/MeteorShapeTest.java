package de.raumfahrt.core;

import org.junit.jupiter.api.Test;

import java.awt.Polygon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeteorShapeTest {

    @Test
    void polygonHatZehnEcken() {
        MeteorShape shape = new MeteorShape(7);

        Polygon polygon = shape.polygon(20);

        assertEquals(10, polygon.npoints);
    }

    @Test
    void gleicherSeedErzeugtGleicheForm() {
        Polygon first = new MeteorShape(42).polygon(20);
        Polygon second = new MeteorShape(42).polygon(20);

        assertEquals(first.xpoints.length, second.xpoints.length);
        for (int i = 0; i < first.npoints; i++) {
            assertEquals(first.xpoints[i], second.xpoints[i]);
            assertEquals(first.ypoints[i], second.ypoints[i]);
        }
    }

    @Test
    void polygonLiegtInnerhalbDerGroessenBounds() {
        Polygon polygon = new MeteorShape(9).polygon(30);

        for (int i = 0; i < polygon.npoints; i++) {
            assertTrue(Math.hypot(polygon.xpoints[i], polygon.ypoints[i]) <= 30 * 1.3 + 1);
        }
    }

    @Test
    void verschiedeneSeedsErzeugenVerschiedeneFormen() {
        Polygon first = new MeteorShape(1).polygon(20);
        Polygon second = new MeteorShape(2).polygon(20);

        boolean differs = false;
        for (int i = 0; i < first.npoints; i++) {
            if (first.xpoints[i] != second.xpoints[i]) {
                differs = true;
                break;
            }
        }
        assertTrue(differs);
    }
}
