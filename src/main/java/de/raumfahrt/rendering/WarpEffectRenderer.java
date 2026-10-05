package de.raumfahrt.rendering;

import de.raumfahrt.core.MonitorSide;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WarpEffectRenderer {

    private static final Color CORE_COLOR = new Color(0xE6, 0xF0, 0xFF);
    private static final Color FINE_COLOR = new Color(0xB4, 0xC8, 0xFF);
    private static final Color MEDIUM_COLOR = new Color(0xD6, 0xE6, 0xFF);
    private static final Color BRIGHT_COLOR = new Color(0xF6, 0xFA, 0xFF);
    private static final Color VIOLET_COLOR = new Color(0xC0, 0xA2, 0xFF);

    private static final double FADE_IN_END = 0.22;
    private static final double FADE_OUT_START = 0.72;
    private static final double TAU = Math.PI * 2.0;

    private final Layer fineLayer;
    private final Layer mediumLayer;
    private final Layer brightLayer;

    public WarpEffectRenderer() {
        this(new Random(0x5EED639L));
    }

    public WarpEffectRenderer(Random random) {
        this.fineLayer = createLayer(random, 240, 0.03, 0.10, 90, 1.0f, FINE_COLOR);
        this.mediumLayer = createLayer(random, 74, 0.05, 0.22, 150, 2.0f, MEDIUM_COLOR);
        this.brightLayer = createBrightLayer(random, 16);
    }

    public void render(Graphics2D graphics, MonitorSide side, double width, double height, double progress) {
        render(graphics, viewportFor(side, width, height), progress);
    }

    void render(Graphics2D graphics, WarpViewport viewport, double progress) {
        double intensity = intensity(progress);
        if (intensity <= 0.0) {
            return;
        }
        drawCore(graphics, viewport, intensity);
        drawLayer(graphics, viewport, intensity, fineLayer);
        drawLayer(graphics, viewport, intensity, mediumLayer);
        drawLayer(graphics, viewport, intensity, brightLayer);
    }

    static WarpViewport viewportFor(MonitorSide side, double width, double height) {
        double centerY = height / 2.0;
        double vanishingX = side == MonitorSide.LEFT ? width * 1.7 : -width * 0.7;
        double coreX = side == MonitorSide.LEFT ? width : 0.0;
        double nearRadius = Math.abs(vanishingX) < Math.abs(vanishingX - width)
                ? Math.abs(vanishingX)
                : Math.abs(vanishingX - width);
        double farRadius = Math.hypot(Math.max(vanishingX, width - vanishingX), centerY);
        return new WarpViewport(vanishingX, centerY, coreX, centerY, nearRadius, farRadius);
    }

    static double intensity(double progress) {
        double clamped = Math.max(0.0, Math.min(1.0, progress));
        if (clamped < FADE_IN_END) {
            return smooth(clamped / FADE_IN_END);
        }
        if (clamped > FADE_OUT_START) {
            return smooth((1.0 - clamped) / (1.0 - FADE_OUT_START));
        }
        return 1.0;
    }

    private static double smooth(double value) {
        double clamped = Math.max(0.0, Math.min(1.0, value));
        return clamped * clamped * (3.0 - 2.0 * clamped);
    }

    private static int alpha(float baseAlpha, double intensity) {
        int value = (int) Math.round(baseAlpha * intensity);
        return Math.max(0, Math.min(255, value));
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private Layer createLayer(
            Random random, int count, double minStart, double length, int alpha, float strokeWidth, Color color) {
        List<Streak> streaks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            streaks.add(new Streak(
                    random.nextDouble() * TAU,
                    minStart + random.nextDouble() * (1.0 - minStart),
                    length * (0.6 + random.nextDouble() * 0.8),
                    null,
                    alpha));
        }
        return new Layer(streaks, strokeWidth, color);
    }

    private Layer createBrightLayer(Random random, int count) {
        List<Streak> streaks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            boolean violet = i % 5 == 4;
            streaks.add(new Streak(
                    random.nextDouble() * TAU,
                    0.02 + random.nextDouble() * 0.4,
                    0.35 + random.nextDouble() * 0.3,
                    violet ? VIOLET_COLOR : BRIGHT_COLOR,
                    220));
        }
        return new Layer(streaks, 3.4f, BRIGHT_COLOR);
    }

    private void drawLayer(Graphics2D graphics, WarpViewport viewport, double intensity, Layer layer) {
        double acceleration = 0.3 + 0.7 * intensity;
        double span = viewport.radiusSpan();
        graphics.setStroke(new BasicStroke(layer.strokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Streak streak : layer.streaks()) {
            Color color = streak.color() != null ? streak.color() : layer.baseColor();
            graphics.setColor(withAlpha(color, alpha(streak.alpha(), intensity)));
            double start = viewport.minRadius() + streak.startFraction() * span;
            double length = streak.lengthFraction() * span * acceleration;
            double cos = Math.cos(streak.angle());
            double sin = Math.sin(streak.angle());
            int innerX = (int) Math.round(viewport.vanishingX() + cos * start);
            int innerY = (int) Math.round(viewport.vanishingY() + sin * start);
            int outerX = (int) Math.round(viewport.vanishingX() + cos * (start + length));
            int outerY = (int) Math.round(viewport.vanishingY() + sin * (start + length));
            graphics.drawLine(innerX, innerY, outerX, outerY);
        }
        graphics.setStroke(new BasicStroke(1.0f));
    }

    private void drawCore(Graphics2D graphics, WarpViewport viewport, double intensity) {
        float radius = (float) (viewport.radiusSpan() * 0.3);
        if (radius <= 0.0f) {
            return;
        }
        int centerAlpha = (int) Math.round(0x55 * intensity);
        RadialGradientPaint core = new RadialGradientPaint(
                new Point2D.Double(viewport.coreX(), viewport.coreY()),
                radius,
                new float[] {0.0f, 0.5f, 1.0f},
                new Color[] {
                    withAlpha(CORE_COLOR, centerAlpha), withAlpha(CORE_COLOR, centerAlpha / 3), withAlpha(CORE_COLOR, 0)
                });
        graphics.setPaint(core);
        graphics.fill(
                new Ellipse2D.Double(viewport.coreX() - radius, viewport.coreY() - radius, radius * 2.0, radius * 2.0));
        graphics.setPaint(null);
    }

    private record Streak(double angle, double startFraction, double lengthFraction, Color color, int alpha) {}

    private record Layer(List<Streak> streaks, float strokeWidth, Color baseColor) {}

    record WarpViewport(
            double vanishingX, double vanishingY, double coreX, double coreY, double minRadius, double maxRadius) {

        double radiusSpan() {
            return maxRadius - minRadius;
        }
    }
}
