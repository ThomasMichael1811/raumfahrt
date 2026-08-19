package de.raumfahrt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RaumfahrtAppTest {

    @Test
    void startupMessageGibtStartmeldungZurueck() {
        assertEquals("Raumfahrt gestartet.", RaumfahrtApp.startupMessage());
    }

    @Test
    void mainStartetOhneExceptions() {
        assertDoesNotThrow(() -> RaumfahrtApp.main(new String[0]));
    }
}