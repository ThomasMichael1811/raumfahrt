package de.raumfahrt.app;

import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
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
        Rectangle screen = screenBounds();
        int width = screen.width;
        int height = screen.height;
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(width, starGenerator.generate(width, height, new Random()));
        MeteorField meteorField = new MeteorField(width, 2, new MeteorSpawner(new Random(), height));
        spacePanel = new SpacePanel(
                new SpaceRenderer(),
                new StarFieldRenderer(),
                new MeteorRenderer(),
                new CabinFrameRenderer(),
                starField,
                meteorField);
        setContentPane(spacePanel);
        setUndecorated(true);
        setSize(width, height);
        setLocation(screen.x, screen.y);
        setExtendedState(MAXIMIZED_BOTH);
        bindEscapeToClose();
        setVisible(true);
        spacePanel.startGameLoop();
    }

    @Override
    public void dispose() {
        spacePanel.stopGameLoop();
        super.dispose();
    }

    private Rectangle screenBounds() {
        GraphicsDevice device =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return device.getDefaultConfiguration().getBounds();
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
