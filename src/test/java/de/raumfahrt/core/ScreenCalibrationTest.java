package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ScreenCalibrationTest {

    @Test
    void focalEntsprichtAbstandMalPixelProCm() {
        ScreenCalibration calibration = new ScreenCalibration(100.0, 53.0);

        double focal = calibration.focalPx(1920.0);

        assertEquals(100.0 * 1920.0 / 53.0, focal, 1e-9);
    }

    @Test
    void einMeterAbstandLiefertRealistischenFocal() {
        ScreenCalibration calibration = new ScreenCalibration(100.0, 50.0);

        assertTrue(calibration.focalPx(1920.0) > 3000.0);
    }

    @Test
    void negativerPixelwertWirdAbgelehnt() {
        ScreenCalibration calibration = new ScreenCalibration(100.0, 53.0);

        assertThrows(IllegalArgumentException.class, () -> calibration.focalPx(0.0));
        assertThrows(IllegalArgumentException.class, () -> calibration.focalPx(-1.0));
    }

    @Test
    void abstandAusserhalbBereichWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () -> new ScreenCalibration(19.9, 53.0));
        assertThrows(IllegalArgumentException.class, () -> new ScreenCalibration(500.1, 53.0));
    }

    @Test
    void bildschirmbreiteAusserhalbBereichWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () -> new ScreenCalibration(100.0, 19.9));
        assertThrows(IllegalArgumentException.class, () -> new ScreenCalibration(100.0, 200.1));
    }
}
