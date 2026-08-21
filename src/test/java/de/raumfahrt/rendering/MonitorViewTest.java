package de.raumfahrt.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.raumfahrt.core.MonitorPairProjection;
import org.junit.jupiter.api.Test;

class MonitorViewTest {

    private static final int W = 1000;
    private static final int H = 800;
    private static final double FOCAL = 400.0;

    @Test
    void centeredProjiziertWeltursprungInDieMitte() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, FOCAL);

        assertEquals(W / 2.0, MonitorView.CENTERED.screenX(projection, 0.0, 500.0), 1e-9);
    }

    @Test
    void linkerMonitorZeigtWeltLinkeSeiteDerLuecke() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, FOCAL);

        double localX = MonitorView.LEFT.screenX(projection, -625.0, 500.0);

        assertEquals(W / 2.0, localX, 1e-9);
    }

    @Test
    void rechterMonitorZeigtWeltRechteSeiteDerLuecke() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, FOCAL);

        double localX = MonitorView.RIGHT.screenX(projection, 625.0, 500.0);

        assertEquals(W / 2.0, localX, 1e-9);
    }

    @Test
    void uebergangAnDerLueckeIstNahtlos() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, FOCAL);

        double leftAtOrigin = MonitorView.LEFT.screenX(projection, 0.0, 500.0);
        double rightAtOrigin = MonitorView.RIGHT.screenX(projection, 0.0, 500.0);

        assertEquals((double) W, leftAtOrigin, 1e-9);
        assertEquals(0.0, rightAtOrigin, 1e-9);
    }

    @Test
    void sichtbereicheBeiderMonitoreUeberlappenNicht() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 100.0, FOCAL);
        double depth = 500.0;

        for (double worldX = -400.0; worldX <= 400.0; worldX += 5.0) {
            boolean leftVisible = MonitorView.LEFT.screenX(projection, worldX, depth) < W;
            boolean rightVisible = MonitorView.RIGHT.screenX(projection, worldX, depth) >= 0.0;
            assertTrue(leftVisible != rightVisible || (!leftVisible && !rightVisible));
        }
    }
}
