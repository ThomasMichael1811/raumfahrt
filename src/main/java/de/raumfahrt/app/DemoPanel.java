package de.raumfahrt.app;

import de.raumfahrt.core.DemoSimulation;
import de.raumfahrt.core.GameLoop;
import de.raumfahrt.core.MonitorPairProjection;
import de.raumfahrt.rendering.ProjectedObject;
import de.raumfahrt.rendering.ProjectionRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

public final class DemoPanel extends JPanel {

    private static final int UPDATES_PER_SECOND = 60;
    private static final double DEPTH = 300.0;
    private static final double PX_PER_CM = 10.0;
    private static final double FOCAL_PX = 400.0;
    private static final double BALL_RADIUS_PX = 12.0;
    private static final double START_SPEED = 50.0;
    private static final Color BALL_COLOR = new Color(0xFF, 0x6A, 0x00);

    private final int viewportWidth;
    private final int viewportHeight;
    private final transient ProjectionRenderer renderer;
    private final transient GameLoop gameLoop;
    private transient DemoSimulation simulation;
    private transient BufferedImage offscreen;

    public DemoPanel(int width, int height, double gapCm) {
        setPreferredSize(new Dimension(width, height));
        this.viewportWidth = width / 2;
        this.viewportHeight = height;
        this.renderer = new ProjectionRenderer();
        this.simulation = createSimulation(gapCm);
        this.gameLoop = new GameLoop(UPDATES_PER_SECOND, deltaSeconds -> {
            simulation.advance(deltaSeconds);
            repaint();
        });
    }

    private DemoSimulation createSimulation(double gapCm) {
        MonitorPairProjection projection =
                new MonitorPairProjection(viewportWidth, viewportHeight, gapCm * PX_PER_CM, FOCAL_PX);
        return new DemoSimulation(projection, DEPTH, START_SPEED);
    }

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    public void setGapCm(double gapCm) {
        simulation = createSimulation(gapCm);
    }

    public void setSpeed(double speed) {
        simulation.setSpeed(speed);
    }

    public double measuredGapTime() {
        return simulation.measuredGapTime();
    }

    public double expectedGapTime() {
        return simulation.expectedGapTime();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int width = getWidth();
        int height = getHeight();
        if (offscreen == null || offscreen.getWidth() != width || offscreen.getHeight() != height) {
            offscreen = new BufferedImage(Math.max(width, 1), Math.max(height, 1), BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D target = offscreen.createGraphics();
        target.drawImage(
                renderer.render(
                        new MonitorPairProjection(viewportWidth, viewportHeight, gapCm() * PX_PER_CM, FOCAL_PX),
                        List.of(new ProjectedObject(simulation.worldX(), 0.0, DEPTH, BALL_RADIUS_PX, BALL_COLOR))),
                0,
                0,
                null);
        drawHud(target, width, height);
        target.dispose();
        graphics.drawImage(offscreen, 0, 0, null);
    }

    private double gapCm() {
        return simulation.projection().gapPx() / PX_PER_CM;
    }

    private void drawHud(Graphics2D target, int width, int height) {
        target.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        target.setColor(Color.WHITE);
        int line = 24;
        target.drawString(String.format("Luecke: %.0f cm", gapCm()), 16, line);
        target.drawString(String.format("Geschwindigkeit: %.0f cm/s", simulation.speed()), 16, line + 24);
        target.drawString(String.format("Erwartete Lueckenzeit: %.3f s", expectedGapTime()), 16, line + 48);
        target.drawString(String.format("Gemessene Lueckenzeit: %.3f s", measuredGapTime()), 16, line + 72);
        double deviation = measuredGapTime() - expectedGapTime();
        target.drawString(String.format("Abweichung: %+.3f s", deviation), 16, line + 96);
        int gapPx = (int) Math.round(gapCm() * PX_PER_CM);
        target.setColor(Color.WHITE);
        target.drawLine(width / 2, 0, width / 2, height);
        target.drawLine(width / 2 + gapPx, 0, width / 2 + gapPx, height);
    }
}
