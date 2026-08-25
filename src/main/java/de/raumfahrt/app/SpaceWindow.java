package de.raumfahrt.app;

import de.raumfahrt.core.EffectDispatcher;
import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.MonitorConfig;
import de.raumfahrt.core.SceneType;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.core.Sun;
import de.raumfahrt.core.WarpScheduler;
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
        int width = screen.width, height = screen.height;
        double focalPx = MonitorConfig.load().calibration().focalPx(width);
        WorldObjects wo = createWorld(width, height, focalPx);
        spacePanel = new SpacePanel(
                new SpaceRenderer(), new StarFieldRenderer(), new MeteorRenderer(), new CabinFrameRenderer(), wo.world);
        spacePanel.setFocalPx(focalPx);
        setContentPane(spacePanel);
        setUndecorated(true);
        setSize(width, height);
        setLocation(screen.x, screen.y);
        setExtendedState(MAXIMIZED_BOTH);
        bindEscapeToClose();
        bindEffectKeys(wo.effectDispatcher);
        setVisible(true);
        gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            wo.warpScheduler.update(deltaSeconds);
            wo.world.update(deltaSeconds);
            spacePanel.repaint();
        });
        gameLoop.start();
    }

    private WorldObjects createWorld(int width, int height, double focalPx) {
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(width, starGenerator.generate(width, height, new Random()));
        MeteorField meteorField = new MeteorField(width, 3, new MeteorSpawner(new Random(), width, height, focalPx));
        Sun sun = new Sun(width, height * 0.3, Math.min(width, height) * 0.3, 5.0);
        SimulationWorld world = new SimulationWorld(width, starField, meteorField, sun);
        WarpScheduler warpScheduler = new WarpScheduler(new Random(), world.warpState(), world::switchScene);
        EffectDispatcher effectDispatcher = new EffectDispatcher();
        registerEffects(effectDispatcher, meteorField, world, warpScheduler);
        return new WorldObjects(world, warpScheduler, effectDispatcher);
    }

    private record WorldObjects(
            SimulationWorld world, WarpScheduler warpScheduler, EffectDispatcher effectDispatcher) {}

    private void registerEffects(
            EffectDispatcher effectDispatcher,
            MeteorField meteorField,
            SimulationWorld world,
            WarpScheduler warpScheduler) {
        effectDispatcher.register(1, meteorField::spawnAimedMeteor);
        effectDispatcher.register(2, () -> meteorField.spawnCrossingMeteor(true));
        effectDispatcher.register(3, () -> meteorField.spawnCrossingMeteor(false));
        effectDispatcher.register(0, warpScheduler::triggerNow);
        effectDispatcher.register(5, () -> world.setScene(SceneType.SMALL_SUN_LEFT));
        effectDispatcher.register(6, () -> world.setScene(SceneType.NO_SUN));
        effectDispatcher.register(7, () -> world.setScene(SceneType.RED_SUN));
        effectDispatcher.register(8, () -> world.setScene(SceneType.TWO_SUNS));
        effectDispatcher.register(9, () -> world.setScene(SceneType.COMET));
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
        for (int key = 0; key <= 9; key++) {
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
