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

    private final Path file;
    private double gapCm;

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

    public void setGapCm(double gapCm) {
        if (!isValid(gapCm)) {
            throw new IllegalArgumentException(
                    "Monitor-Abstand muss zwischen " + MIN_GAP_CM + " und " + MAX_GAP_CM + " cm liegen: " + gapCm);
        }
        this.gapCm = gapCm;
    }

    public static boolean isValid(double gapCm) {
        return gapCm >= MIN_GAP_CM && gapCm <= MAX_GAP_CM;
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty(KEY_GAP_CM, Double.toString(gapCm));
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
    }
}
