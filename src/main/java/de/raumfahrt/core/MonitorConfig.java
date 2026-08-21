package de.raumfahrt.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class MonitorConfig {

    public static final double MIN_GAP_CM = 0.0;
    public static final double MAX_GAP_CM = 50.0;
    public static final double DEFAULT_GAP_CM = 20.0;
    private static final String KEY_GAP_CM = "monitor.gapCm";
    private static final String KEY_VIEWING_DISTANCE_CM = "view.distanceCm";
    private static final String KEY_SCREEN_WIDTH_CM = "view.screenWidthCm";

    private final Path file;
    private double gapCm;
    private ScreenCalibration calibration =
            new ScreenCalibration(ScreenCalibration.DEFAULT_DISTANCE_CM, ScreenCalibration.DEFAULT_WIDTH_CM);

    public MonitorConfig(Path file) {
        this.file = file;
        this.gapCm = DEFAULT_GAP_CM;
    }

    public static MonitorConfig load() {
        Path file = Path.of(System.getProperty("user.home"), ".raumfahrt", "config.properties");
        MonitorConfig config = new MonitorConfig(file);
        config.readFromFile();
        return config;
    }

    public double gapCm() {
        return gapCm;
    }

    public ScreenCalibration calibration() {
        return calibration;
    }

    public void setGapCm(double gapCm) {
        if (!isValid(gapCm)) {
            throw new IllegalArgumentException(
                    "Monitor-Abstand muss zwischen " + MIN_GAP_CM + " und " + MAX_GAP_CM + " cm liegen: " + gapCm);
        }
        this.gapCm = gapCm;
    }

    public void setCalibration(ScreenCalibration calibration) {
        this.calibration = calibration;
    }

    public static boolean isValid(double gapCm) {
        return gapCm >= MIN_GAP_CM && gapCm <= MAX_GAP_CM;
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty(KEY_GAP_CM, Double.toString(gapCm));
        properties.setProperty(KEY_VIEWING_DISTANCE_CM, Double.toString(calibration.viewingDistanceCm()));
        properties.setProperty(KEY_SCREEN_WIDTH_CM, Double.toString(calibration.screenWidthCm()));
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (OutputStream output = Files.newOutputStream(file)) {
                properties.store(output, "Raumfahrt Konfiguration");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Konfiguration konnte nicht gespeichert werden: " + file, e);
        }
    }

    void readFromFile() {
        if (!Files.exists(file)) {
            return;
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Konfiguration konnte nicht geladen werden: " + file, e);
        }
        String value = properties.getProperty(KEY_GAP_CM);
        if (value != null) {
            try {
                double parsed = Double.parseDouble(value);
                if (isValid(parsed)) {
                    gapCm = parsed;
                }
            } catch (NumberFormatException e) {
                gapCm = DEFAULT_GAP_CM;
            }
        }
        calibration = readCalibration(properties);
    }

    private ScreenCalibration readCalibration(Properties properties) {
        try {
            return new ScreenCalibration(
                    parseValue(properties, KEY_VIEWING_DISTANCE_CM, ScreenCalibration.DEFAULT_DISTANCE_CM),
                    parseValue(properties, KEY_SCREEN_WIDTH_CM, ScreenCalibration.DEFAULT_WIDTH_CM));
        } catch (IllegalArgumentException e) {
            return new ScreenCalibration(ScreenCalibration.DEFAULT_DISTANCE_CM, ScreenCalibration.DEFAULT_WIDTH_CM);
        }
    }

    private double parseValue(Properties properties, String key, double fallback) {
        String value = properties.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
