package de.raumfahrt.app;

import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public final class SpaceWindow extends JFrame {

    private final transient SpacePanel spacePanel;

    public SpaceWindow() {
        super("Raumfahrt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(1280, starGenerator.generate(1280, 720, new Random()));
        MeteorField meteorField = new MeteorField(1280, 6, new MeteorSpawner(new Random(), 720));
        spacePanel = new SpacePanel(
                new SpaceRenderer(),
                new StarFieldRenderer(),
                new MeteorRenderer(),
                new CabinFrameRenderer(),
                starField,
                meteorField);
        setContentPane(spacePanel);
        setPreferredSize(new Dimension(1280, 720));
        pack();
        setLocationRelativeTo(null);
        bindEscapeToClose();
        setVisible(true);
        spacePanel.startGameLoop();
    }

    @Override
    public void dispose() {
        spacePanel.stopGameLoop();
        super.dispose();
    }

    private void bindEscapeToClose() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
    }
}
