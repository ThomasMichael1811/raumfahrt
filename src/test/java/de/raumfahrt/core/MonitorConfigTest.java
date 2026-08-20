package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MonitorConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void defaultWertIst20Cm() {
        MonitorConfig config = new MonitorConfig(tempDir.resolve("config.properties"));

        assertEquals(20.0, config.gapCm());
    }

    @Test
    void setGapCmSpeichertWert() {
        MonitorConfig config = new MonitorConfig(tempDir.resolve("config.properties"));

        config.setGapCm(30.0);

        assertEquals(30.0, config.gapCm());
    }

    @Test
    void ungueltigerWertWirdAbgelehnt() {
        MonitorConfig config = new MonitorConfig(tempDir.resolve("config.properties"));

        assertThrows(IllegalArgumentException.class, () -> config.setGapCm(-1.0));
        assertThrows(IllegalArgumentException.class, () -> config.setGapCm(51.0));
    }

    @Test
    void isValidPrueftBereich() {
        assertTrue(MonitorConfig.isValid(0.0));
        assertTrue(MonitorConfig.isValid(25.0));
        assertTrue(MonitorConfig.isValid(50.0));
        assertFalse(MonitorConfig.isValid(-0.1));
        assertFalse(MonitorConfig.isValid(50.1));
    }

    @Test
    void saveUndLoadRundreise() throws IOException {
        Path file = tempDir.resolve("config.properties");
        MonitorConfig config = new MonitorConfig(file);
        config.setGapCm(35.0);
        config.save();

        assertTrue(Files.exists(file));
        MonitorConfig loaded = new MonitorConfig(file);
        loaded.readFromFile();

        assertEquals(35.0, loaded.gapCm());
    }

    @Test
    void kaputteDateiFaelltAufDefaultZurueck() throws IOException {
        Path file = tempDir.resolve("config.properties");
        Files.writeString(file, "monitor.gapCm=abc");
        MonitorConfig config = new MonitorConfig(file);
        config.readFromFile();

        assertEquals(20.0, config.gapCm());
    }
}
