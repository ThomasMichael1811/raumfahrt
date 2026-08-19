package de.raumfahrt;

import de.raumfahrt.app.SpaceWindow;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

public final class RaumfahrtApp {

    private RaumfahrtApp() {
    }

    public static void main(String[] args) {
        launch();
    }

    static void launch() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println(startupMessage());
            return;
        }
        SwingUtilities.invokeLater(SpaceWindow::new);
    }

    static String startupMessage() {
        return "Raumfahrt gestartet.";
    }
}
