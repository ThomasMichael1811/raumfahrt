package de.raumfahrt.app;

import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Random;

public final class SpaceWindow extends JFrame {

    public SpaceWindow() {
        super("Raumfahrt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        StarGenerator starGenerator = new StarGenerator();
        setContentPane(new SpacePanel(new SpaceRenderer(), new StarFieldRenderer(), new CabinFrameRenderer(),
            starGenerator.generate(1280, 720, new Random())));
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



