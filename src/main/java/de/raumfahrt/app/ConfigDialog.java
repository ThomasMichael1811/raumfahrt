package de.raumfahrt.app;

import de.raumfahrt.core.MonitorConfig;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class ConfigDialog extends JDialog {

    private static final int COLUMNS = 8;

    private final transient MonitorConfig config;
    private final transient JTextField gapField;
    private final transient JLabel errorLabel;

    public ConfigDialog(JFrame owner, MonitorConfig config) {
        super(owner, "Konfiguration", true);
        this.config = config;
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        form.add(new JLabel("Monitor-Abstand (cm):"));
        gapField = new JTextField(COLUMNS);
        gapField.setText(String.format("%.0f", config.gapCm()));
        form.add(gapField);
        add(form, BorderLayout.CENTER);
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(errorLabel, BorderLayout.SOUTH);
        JPanel buttons = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(event -> save());
        buttons.add(saveButton);
        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(event -> dispose());
        buttons.add(cancelButton);
        add(buttons, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void save() {
        try {
            double gap = Double.parseDouble(gapField.getText().trim());
            config.setGapCm(gap);
            config.save();
            dispose();
        } catch (NumberFormatException e) {
            errorLabel.setText("Ungueltige Zahl.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
