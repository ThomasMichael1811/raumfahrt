package de.raumfahrt.app;

import de.raumfahrt.rendering.SpaceRenderer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public final class SpaceWindow extends JFrame {

    public SpaceWindow() {
        super("Raumfahrt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new SpacePanel(new SpaceRenderer()));
        setPreferredSize(new Dimension(1280, 720));
        pack();
        setLocationRelativeTo(null);
        bindEscapeToClose();
        setVisible(true);
    }

    private void bindEscapeToClose() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
    }
}

