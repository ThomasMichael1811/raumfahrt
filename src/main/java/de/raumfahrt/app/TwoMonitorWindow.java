package de.raumfahrt.app;

import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.MeteorField;
import de.raumfahrt.core.MeteorSpawner;
import de.raumfahrt.core.MonitorConfig;
import de.raumfahrt.core.MonitorSide;
import de.raumfahrt.core.SceneType;
import de.raumfahrt.core.SimulationWorld;
import de.raumfahrt.core.StarField;
import de.raumfahrt.core.StarGenerator;
import de.raumfahrt.core.Sun;
import de.raumfahrt.core.WarpScheduler;
import de.raumfahrt.core.WarpTransitionController;
import de.raumfahrt.rendering.CabinFrameRenderer;
import de.raumfahrt.rendering.MeteorRenderer;
import de.raumfahrt.rendering.MonitorView;
import de.raumfahrt.rendering.SpaceRenderer;
import de.raumfahrt.rendering.StarFieldRenderer;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public final class TwoMonitorWindow {

    private static final int UPDATES_PER_SECOND = 60;
    private static final int PAN_SPEED = 30;

    private final transient SimulationWorld world;
    private final transient GameLoop gameLoop;
    private final transient JFrame windowOne;
    private final transient JFrame windowTwo;
    private final transient WarpTransitionController controller;
    private final transient WarpScheduler warpScheduler;
    private transient int panDirection;

    public TwoMonitorWindow() {
        GraphicsDevice[] devices = sortedDevices();
        Simulation simulation = createSimulation(devices);
        world = simulation.world();
        controller = simulation.controller();
        warpScheduler = simulation.warpScheduler();
        windowOne =
                createWindow(devices[0], "Raumfahrt links", MonitorView.LEFT, simulation.focalPx(), simulation.side());
        windowTwo = createWindow(
                devices.length > 1 ? devices[1] : devices[0],
                "Raumfahrt rechts",
                MonitorView.RIGHT,
                simulation.focalPx(),
                simulation.side());
        bindInput(windowOne);
        bindInput(windowTwo);
        setVisible();
        gameLoop = new GameLoop(UPDATES_PER_SECOND, this::step);
        gameLoop.start();
    }

    private Simulation createSimulation(GraphicsDevice[] devices) {
        Rectangle primary = devices[0].getDefaultConfiguration().getBounds();
        int width = primary.width;
        int height = primary.height;
        StarGenerator starGenerator = new StarGenerator();
        StarField starField = new StarField(width, starGenerator.generate(width, height, new Random()));
        MonitorConfig config = MonitorConfig.load();
        double focalPx = config.calibration().focalPx(width);
        MeteorField meteorField = new MeteorField(width, 3, new MeteorSpawner(new Random(), width, height, focalPx));
        Sun sun = new Sun(width, height * 0.3, Math.min(width, height) * 0.3, 5.0);
        SimulationWorld simulationWorld = new SimulationWorld(width, starField, meteorField, sun);
        WarpTransitionController transitionController = new WarpTransitionController(
                simulationWorld.warpState(),
                simulationWorld::setScene,
                simulationWorld::scene,
                simulationWorld::isPaused);
        WarpScheduler scheduler = new WarpScheduler(new Random(), transitionController, simulationWorld::nextScene);
        return new Simulation(simulationWorld, transitionController, scheduler, focalPx, config.side());
    }

    private void step(double deltaSeconds) {
        if (!world.isPaused()) {
            controller.update(deltaSeconds);
            warpScheduler.update(deltaSeconds);
        }
        world.moveCamera(panDirection, deltaSeconds);
        world.update(deltaSeconds);
        windowOne.getContentPane().repaint();
        windowTwo.getContentPane().repaint();
    }

    private record Simulation(
            SimulationWorld world,
            WarpTransitionController controller,
            WarpScheduler warpScheduler,
            double focalPx,
            MonitorSide side) {}

    private JFrame createWindow(
            GraphicsDevice device, String title, MonitorView view, double focalPx, MonitorSide side) {
        Rectangle bounds = device.getDefaultConfiguration().getBounds();
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SpacePanel panel = new SpacePanel(
                new SpaceRenderer(),
                new StarFieldRenderer(),
                new MeteorRenderer(),
                new CabinFrameRenderer(),
                world,
                view,
                side);
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
        bindAction(frame, "0", "warp", controller::startIndependentWarp);
        bindAnimatedGifAction(frame.getRootPane(), world::spawnAnimatedGifMeteor);
        bindAnimatedGif2Action(frame.getRootPane(), world::spawnAnimatedGif2Meteor);
        bindAction(frame, "4", "sceneNormal", () -> controller.startSceneTransition(SceneType.NORMAL));
        bindAction(frame, "5", "sceneSmallSun", () -> controller.startSceneTransition(SceneType.SMALL_SUN_LEFT));
        bindAction(frame, "6", "sceneNoSun", () -> controller.startSceneTransition(SceneType.NO_SUN));
        bindAction(frame, "7", "sceneRedSun", () -> controller.startSceneTransition(SceneType.RED_SUN));
        bindAction(frame, "8", "sceneTwoSuns", () -> controller.startSceneTransition(SceneType.TWO_SUNS));
        bindAction(frame, "9", "sceneComet", () -> controller.startSceneTransition(SceneType.COMET));
        bindAction(frame, "released LEFT", "panStop", () -> panDirection = 0);
        bindAction(frame, "released RIGHT", "panStop", () -> panDirection = 0);
    }

    private void bindAction(JFrame frame, String keyStroke, String name, Runnable action) {
        bindAction(frame.getRootPane(), keyStroke, name, action);
    }

    static void bindAnimatedGifAction(JComponent root, Runnable action) {
        bindAction(root, "2", "spawnAnimatedGifMeteor", action);
    }

    static void bindAnimatedGif2Action(JComponent root, Runnable action) {
        bindAction(root, "3", "spawnAnimatedGif2Meteor", action);
    }

    private static void bindAction(JComponent root, String keyStroke, String name, Runnable action) {
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), name);
        root.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }

    private static GraphicsDevice[] sortedDevices() {
        GraphicsDevice[] devices =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        Arrays.sort(
                devices,
                (a, b) -> Integer.compare(
                        a.getDefaultConfiguration().getBounds().x,
                        b.getDefaultConfiguration().getBounds().x));
        return devices;
    }

    public void dispose() {
        gameLoop.stop();
        windowOne.dispose();
        windowTwo.dispose();
    }
}
