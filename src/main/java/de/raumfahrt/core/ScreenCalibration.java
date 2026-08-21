package de.raumfahrt.core;

public record ScreenCalibration(double viewingDistanceCm, double screenWidthCm) {

    public static final double MIN_DISTANCE_CM = 20.0;
    public static final double MAX_DISTANCE_CM = 500.0;
    public static final double MIN_WIDTH_CM = 20.0;
    public static final double MAX_WIDTH_CM = 200.0;
    public static final double DEFAULT_DISTANCE_CM = 100.0;
    public static final double DEFAULT_WIDTH_CM = 53.0;

    public ScreenCalibration {
        if (!isValidDistance(viewingDistanceCm)) {
            throw new IllegalArgumentException("Betrachtungsabstand muss zwischen " + MIN_DISTANCE_CM + " und "
                    + MAX_DISTANCE_CM + " cm liegen: " + viewingDistanceCm);
        }
        if (!isValidWidth(screenWidthCm)) {
            throw new IllegalArgumentException("Monitorbreite muss zwischen " + MIN_WIDTH_CM + " und " + MAX_WIDTH_CM
                    + " cm liegen: " + screenWidthCm);
        }
    }

    public static boolean isValidDistance(double viewingDistanceCm) {
        return viewingDistanceCm >= MIN_DISTANCE_CM && viewingDistanceCm <= MAX_DISTANCE_CM;
    }

    public static boolean isValidWidth(double screenWidthCm) {
        return screenWidthCm >= MIN_WIDTH_CM && screenWidthCm <= MAX_WIDTH_CM;
    }

    public double focalPx(double screenWidthPx) {
        if (screenWidthPx <= 0) {
            throw new IllegalArgumentException("Bildschirmbreite in Pixeln muss positiv sein: " + screenWidthPx);
        }
        return viewingDistanceCm * screenWidthPx / screenWidthCm;
    }
}
