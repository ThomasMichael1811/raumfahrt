package de.raumfahrt.core;

public record MonitorPairProjection(int monitorWidthPx, int monitorHeightPx, double gapPx, double focalPx) {

    public double screenXLeft(double worldX, double depth) {
        return focalPx * worldX / depth + monitorWidthPx + gapPx / 2.0;
    }

    public double screenXRight(double worldX, double depth) {
        return focalPx * worldX / depth + monitorWidthPx - gapPx / 2.0;
    }

    public double screenXCentered(double worldX, double depth) {
        return focalPx * worldX / depth + monitorWidthPx / 2.0;
    }

    public double screenY(double worldY, double depth) {
        return focalPx * worldY / depth + monitorHeightPx / 2.0;
    }

    public double scale(double worldSize, double depth) {
        return focalPx * worldSize / depth;
    }

    public double leftExitWorldX(double depth) {
        return -gapPx * depth / (2.0 * focalPx);
    }

    public double rightEntryWorldX(double depth) {
        return gapPx * depth / (2.0 * focalPx);
    }
}
