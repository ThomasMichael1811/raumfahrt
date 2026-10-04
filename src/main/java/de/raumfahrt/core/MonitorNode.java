package de.raumfahrt.core;

public record MonitorNode(
    String id,
    String deviceFingerprint,
    boolean primary,
    ScreenCalibration calibration
) {
    public MonitorNode {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Monitor-ID darf nicht null oder leer sein");
        }
        if (calibration == null) {
            throw new IllegalArgumentException("Kalibrierung darf nicht null sein");
        }
    }
}