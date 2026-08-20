package de.raumfahrt;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class RaumfahrtAppTest {

    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void startupMessageGibtStartmeldungZurueck() {
        assertEquals("Raumfahrt gestartet.", RaumfahrtApp.startupMessage());
    }

    @Test
    void launchHeadlessGibtMeldungOhneFenster() {
        System.setProperty("java.awt.headless", "true");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        assertDoesNotThrow(() -> RaumfahrtApp.launch(false));
        assertDoesNotThrow(() -> RaumfahrtApp.launch(true));

        assertTrue(output.toString().contains("Raumfahrt gestartet."));
    }
}
