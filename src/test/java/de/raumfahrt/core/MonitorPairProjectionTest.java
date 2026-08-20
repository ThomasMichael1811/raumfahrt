package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MonitorPairProjectionTest {

    private static final int W = 800;
    private static final int H = 600;

    @Test
    void mittigesObjektErscheintRechtsAufLinkemUndLinksAufRechtemMonitor() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);

        double leftX = projection.screenXLeft(0.0, 500.0);
        double rightX = projection.screenXRight(0.0, 500.0);

        assertEquals(W + 20.0, leftX);
        assertEquals(W - 20.0, rightX);
    }

    @Test
    void objektVerlaesstLinkenUndBetrittRechtenMonitorOhneSprung() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);

        double exitWorldX = projection.leftExitWorldX(500.0);
        double entryWorldX = projection.rightEntryWorldX(500.0);

        assertEquals(W, projection.screenXLeft(exitWorldX, 500.0), 1e-9);
        assertEquals(W, projection.screenXRight(entryWorldX, 500.0), 1e-9);
    }

    @Test
    void nahesObjektVerlaesstLinkenMonitorVorEntferntem() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);

        double nearExit = projection.leftExitWorldX(200.0);
        double farExit = projection.leftExitWorldX(2000.0);

        assertTrue(Math.abs(nearExit) < Math.abs(farExit));
    }

    @Test
    void groessereLueckeVerschiebtProjektionMessbar() {
        MonitorPairProjection small = new MonitorPairProjection(W, H, 20.0, 400.0);
        MonitorPairProjection large = new MonitorPairProjection(W, H, 80.0, 400.0);

        double smallLeft = small.screenXLeft(50.0, 500.0);
        double largeLeft = large.screenXLeft(50.0, 500.0);

        assertTrue(Math.abs(smallLeft - largeLeft) > 1.0);
    }

    @Test
    void bildschirmYZentriertUndSkaliertMitTiefe() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);

        assertEquals(H / 2.0, projection.screenY(0.0, 500.0));
        assertTrue(projection.screenY(10.0, 500.0) > projection.screenY(10.0, 1000.0));
    }

    @Test
    void screenXCenteredZentriertWeltursprung() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, 400.0);

        assertEquals(W / 2.0, projection.screenXCentered(0.0, 500.0));
    }

    @Test
    void screenXCenteredBewegtObjektMitTiefeUndSeite() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 0.0, 400.0);

        assertTrue(projection.screenXCentered(10.0, 500.0) > W / 2.0);
        assertTrue(projection.screenXCentered(-10.0, 500.0) < W / 2.0);
        assertTrue(projection.screenXCentered(10.0, 500.0) > projection.screenXCentered(10.0, 1000.0));
    }

    @Test
    void scaleWaechstMitNaehe() {
        MonitorPairProjection projection = new MonitorPairProjection(W, H, 40.0, 400.0);

        assertTrue(projection.scale(15.0, 200.0) > projection.scale(15.0, 800.0));
        assertEquals(1.0, projection.scale(1.0, 400.0), 1e-9);
    }
}
