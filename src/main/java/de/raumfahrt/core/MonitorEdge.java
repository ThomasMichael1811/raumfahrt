package de.raumfahrt.core;

public record MonitorEdge(String from, String to, double gapCm) {
    public MonitorEdge {
        if (from == null || from.isEmpty()) {
            throw new IllegalArgumentException("Quell-Monitor-ID darf nicht null oder leer sein");
        }
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Ziel-Monitor-ID darf nicht null oder leer sein");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Ein Monitor kann nicht selbst Nachbarn mit sich selbst sein");
        }
        if (gapCm < 0) {
            throw new IllegalArgumentException("Abstand muss nicht-negativ sein: " + gapCm);
        }
    }
}
