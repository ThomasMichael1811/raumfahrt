package de.raumfahrt.app;

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
import de.raumfahrt.rendering.MonitorView;
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

public final class TwoMonitorWindow {

    private static final int UPDATES_PER_SECOND = 60;
    private static final int PAN_SPEED = 300;

    private final transient SimulationWorld world;
    private final transient GameLoop gameLoop;
    private final transient JFrame windowOne;
    private final transient JFrame windowTwo;
    private transient int panDirection;

    public TwoMonitorWindow() {
        GraphicsDevice[] devices =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        Rectangle primary = devices[0].getDefaultConfiguration().getBounds();
        int width = primary.width;
        int height = primary.height;
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(width, starGenerator.generate(width, height, new Random()));
        double focalPx = MonitorConfig.load().calibration().focalPx(width);
        MeteorField meteorField = new MeteorField(width, 2, new MeteorSpawner(new Random(), width, height, focalPx));
        Sun sun = new Sun(width, height * 0.3, Math.min(width, height) * 0.3, 5.0);
        world = new SimulationWorld(width, starField, meteorField, sun);
        windowOne = createWindow(devices[0], "Raumfahrt links", MonitorView.LEFT, focalPx);
        windowTwo = createWindow(
                devices.length > 1 ? devices[1] : devices[0], "Raumfahrt rechts", MonitorView.RIGHT, focalPx);
        bindInput(windowOne);
        bindInput(windowTwo);
        setVisible();
        gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            world.moveCamera(panDirection, deltaSeconds);
            world.update(deltaSeconds);
            windowOne.getContentPane().repaint();
            windowTwo.getContentPane().repaint();
        });
        gameLoop.start();
    }

    private JFrame createWindow(GraphicsDevice device, String title, MonitorView view, double focalPx) {
        Rectangle bounds = device.getDefaultConfiguration().getBounds();
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SpacePanel panel = new SpacePanel(
                new SpaceRenderer(),
                new StarFieldRenderer(),
                new MeteorRenderer(),
                new CabinFrameRenderer(),
                world,
                view);
        panel.setFocalPx(focalPx);
        frame.setContentPane(panel);
        frame.setUndecorated(true);
        frame.setBounds(bounds);
        return frame;
    }

    private void setVisible() {
        windowOne.setVisible(true);
        windowTwo.setVisible(true);
    }

    private void bindInput(JFrame frame) {
        bindAction(frame, "ESCAPE", "close", this::dispose);
        bindAction(frame, "LEFT", "panLeft", () -> panDirection = -PAN_SPEED);
        bindAction(frame, "RIGHT", "panRight", () -> panDirection = PAN_SPEED);
        bindAction(frame, "SPACE", "pause", world::togglePause);
        bindAction(frame, "released LEFT", "panStop", () -> panDirection = 0);
        bindAction(frame, "released RIGHT", "panStop", () -> panDirection = 0);
    }

    private void bindAction(JFrame frame, String keyStroke, String name, Runnable action) {
        JComponent root = frame.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), name);
        root.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }

    public void dispose() {
        gameLoop.stop();
        windowOne.dispose();
        windowTwo.dispose();
    }
}
