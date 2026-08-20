package de.raumfahrt.app;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public final class DemoWindow extends JFrame {

    private static final int VIEWPORT_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MAX_GAP_PX = 500;
    private static final double DEFAULT_GAP_CM = 20.0;

    private final transient DemoPanel demoPanel;

    public DemoWindow() {
        super("Demo: 2-Monitor-Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        demoPanel = new DemoPanel(VIEWPORT_WIDTH * 2 + MAX_GAP_PX, WINDOW_HEIGHT, DEFAULT_GAP_CM);
        setLayout(new BorderLayout());
        add(demoPanel, BorderLayout.CENTER);
        add(buildControls(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        demoPanel.start();
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel();
        controls.add(new JLabel("Luecke (cm):"));
        JSlider gapSlider = new JSlider(0, 50, (int) DEFAULT_GAP_CM);
        gapSlider.addChangeListener((ChangeEvent event) -> demoPanel.setGapCm(gapSlider.getValue()));
        controls.add(gapSlider);
        controls.add(new JLabel("Geschwindigkeit (cm/s):"));
        JSlider speedSlider = new JSlider(0, 200, 50);
        speedSlider.addChangeListener((ChangeEvent event) -> demoPanel.setSpeed(speedSlider.getValue()));
        controls.add(speedSlider);
        return controls;
    }

    @Override
    public void dispose() {
        demoPanel.stop();
        super.dispose();
    }
}
