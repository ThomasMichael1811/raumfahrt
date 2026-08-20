package de.raumfahrt;

import de.raumfahrt.app.DemoWindow;
import de.raumfahrt.app.SpaceWindow;
import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;
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
            if (demo || chooseDemo()) {
                new DemoWindow();
            } else {
                new SpaceWindow();
            }
        });
    }

    private static boolean chooseDemo() {
        Object[] options = {"Normale Sicht", "Demo-Modus"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Bitte Modus wählen:",
                "Raumfahrt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return choice == 1;
    }

    static String startupMessage() {
        return "Raumfahrt gestartet.";
    }
}
