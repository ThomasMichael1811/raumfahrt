package de.raumfahrt.rendering;

import de.raumfahrt.core.MonitorPairProjection;

public enum MonitorView {
    CENTERED,
    LEFT,
    RIGHT;

    public double screenX(MonitorPairProjection projection, double worldX, double depth) {
        return switch (this) {
            case CENTERED -> projection.screenXCentered(worldX, depth);
            case LEFT -> projection.screenXLeft(worldX, depth);
            case RIGHT -> projection.screenXRight(worldX, depth) - projection.monitorWidthPx();
        };
    }
}
