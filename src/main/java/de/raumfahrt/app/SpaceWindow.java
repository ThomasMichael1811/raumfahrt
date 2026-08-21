package de.raumfahrt.app;

import de.raumfahrt.core.EffectDispatcher;
import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.MonitorConfig;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.core.Sun;
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

    private static final int UPDATES_PER_SECOND = 60;

    private final transient SpacePanel spacePanel;
    private final transient GameLoop gameLoop;

    public SpaceWindow() {
        super("Raumfahrt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Rectangle screen = screenBounds();
        int width = screen.width;
        int height = screen.height;
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(width, starGenerator.generate(width, height, new Random()));
        MeteorField meteorField = new MeteorField(width, 2, new MeteorSpawner(new Random(), width, height));
        EffectDispatcher effectDispatcher = new EffectDispatcher();
        effectDispatcher.register(1, meteorField::spawnAimedMeteor);
        Sun sun = new Sun(width, height * 0.3, Math.min(width, height) * 0.3, 5.0);
        SimulationWorld world = new SimulationWorld(width, starField, meteorField, sun);
        spacePanel = new SpacePanel(
                new SpaceRenderer(), new StarFieldRenderer(), new MeteorRenderer(), new CabinFrameRenderer(), world);
        spacePanel.setFocalPx(MonitorConfig.load().calibration().focalPx(width));
        setContentPane(spacePanel);
        setUndecorated(true);
        setSize(width, height);
        setLocation(screen.x, screen.y);
        setExtendedState(MAXIMIZED_BOTH);
        bindEscapeToClose();
        bindEffectKeys(effectDispatcher);
        setVisible(true);
        gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            world.update(deltaSeconds);
            spacePanel.repaint();
        });
        gameLoop.start();
    }

    @Override
    public void dispose() {
        gameLoop.stop();
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

    private void bindEffectKeys(EffectDispatcher effectDispatcher) {
        for (int key = 1; key <= 9; key++) {
            int effectKey = key;
            getRootPane()
                    .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("pressed " + key), "effect" + key);
            getRootPane().getActionMap().put("effect" + key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    effectDispatcher.trigger(effectKey);
                }
            });
        }
    }
}
