package de.raumfahrt.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.junit.jupiter.api.Test;

class TwoMonitorWindowInputTest {

    @Test
    void tasteZweiLoestGifAsteroidAus() {
        JRootPane root = new JRootPane();
        AtomicInteger spawns = new AtomicInteger();
        TwoMonitorWindow.bindAnimatedGifAction(root, spawns::incrementAndGet);
        Object actionName = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke("2"));

        root.getActionMap().get(actionName).actionPerformed(new ActionEvent(root, ActionEvent.ACTION_PERFORMED, "2"));

        assertEquals(1, spawns.get());
    }
}
