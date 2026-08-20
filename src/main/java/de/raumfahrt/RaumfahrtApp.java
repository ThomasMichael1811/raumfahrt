package de.raumfahrt;

import de.raumfahrt.app.DemoWindow;
import de.raumfahrt.app.SpaceWindow;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

public final class RaumfahrtApp {

    private RaumfahrtApp() {}

    public static void main(String[] args) {
        boolean demo = args.length > 0 && "demo".equals(args[0]);
        launch(demo);
    }

    static void launch(boolean demo) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println(startupMessage());
            return;
        }
        SwingUtilities.invokeLater(() -> {
            if (demo) {
                new DemoWindow();
            } else {
                new SpaceWindow();
            }
        });
    }

    static String startupMessage() {
        return "Raumfahrt gestartet.";
    }
}
