package de.raumfahrt.app;

import de.raumfahrt.core.MonitorConfig;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public final class DemoWindow extends JFrame {

    private static final int VIEWPORT_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;

    private final transient DemoPanel demoPanel;
    private final transient MonitorConfig config;

    public DemoWindow() {
        super("Demo: 2-Monitor-Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        config = MonitorConfig.load();
        demoPanel = new DemoPanel(VIEWPORT_WIDTH, WINDOW_HEIGHT, config.gapCm());
        setLayout(new BorderLayout());
        add(demoPanel, BorderLayout.CENTER);
        add(buildControls(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        demoPanel.start();
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel(new FlowLayout());
        controls.add(new JLabel("Luecke (cm):"));
        JSlider gapSlider = new JSlider(0, 50, (int) config.gapCm());
        gapSlider.addChangeListener((ChangeEvent event) -> demoPanel.setGapCm(gapSlider.getValue()));
        controls.add(gapSlider);
        controls.add(new JLabel("Geschwindigkeit (cm/s):"));
        JSlider speedSlider = new JSlider(0, 200, 50);
        speedSlider.addChangeListener((ChangeEvent event) -> demoPanel.setSpeed(speedSlider.getValue()));
        controls.add(speedSlider);
        JButton rulerButton = new JButton("Kalibrierung");
        rulerButton.addActionListener(event -> demoPanel.setRulerVisible(true));
        controls.add(rulerButton);
        JButton configButton = new JButton("Konfiguration");
        configButton.addActionListener(event -> {
            ConfigDialog dialog = new ConfigDialog(this, config);
            dialog.setVisible(true);
            demoPanel.setGapCm(config.gapCm());
            gapSlider.setValue((int) config.gapCm());
        });
        controls.add(configButton);
        return controls;
    }

    @Override
    public void dispose() {
        demoPanel.stop();
        super.dispose();
    }
}
