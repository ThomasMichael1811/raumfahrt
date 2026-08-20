package de.raumfahrt;

import de.raumfahrt.app.DemoWindow;
import de.raumfahrt.app.SpaceWindow;
import de.raumfahrt.app.TwoMonitorWindow;
import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class RaumfahrtApp {

    static final int MODE_NORMAL = 0;
    static final int MODE_DEMO = 1;
    static final int MODE_TWO_MONITORS = 2;

    private RaumfahrtApp() {}

    public static void main(String[] args) {
        String mode = args.length > 0 ? args[0] : "";
        if ("demo".equals(mode)) {
            launch(MODE_DEMO);
        } else if ("2monitor".equals(mode)) {
            launch(MODE_TWO_MONITORS);
        } else {
            launch(MODE_NORMAL);
        }
    }

    static void launch(int mode) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println(startupMessage());
            return;
        }
        SwingUtilities.invokeLater(() -> {
            if (mode == MODE_NORMAL) {
                int choice = chooseMode();
                if (choice == MODE_TWO_MONITORS) {
                    new TwoMonitorWindow();
                } else if (choice == MODE_DEMO) {
                    new DemoWindow();
                } else {
                    new SpaceWindow();
                }
            } else if (mode == MODE_TWO_MONITORS) {
                new TwoMonitorWindow();
            } else {
                new DemoWindow();
            }
        });
    }

    private static int chooseMode() {
        Object[] options = {"Normale Sicht", "Demo-Modus", "2-Monitor-Modus"};
        return JOptionPane.showOptionDialog(
                null,
                "Bitte Modus wählen:",
                "Raumfahrt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
    }

    static String startupMessage() {
        return "Raumfahrt gestartet.";
    }
}
