package de.raumfahrt.app;

import de.raumfahrt.core.MonitorConfig;
import de.raumfahrt.core.ScreenCalibration;
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
    private transient JTextField gapField;
    private transient JTextField distanceField;
    private transient JTextField widthField;
    private final transient JLabel errorLabel;

    public ConfigDialog(JFrame owner, MonitorConfig config) {
        super(owner, "Konfiguration", true);
        this.config = config;
        setLayout(new BorderLayout());
        add(buildForm(), BorderLayout.CENTER);
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

    private JPanel buildForm() {
        JPanel form = new JPanel(new FlowLayout());
        gapField = textField(form, "Monitor-Abstand (cm):", config.gapCm());
        distanceField = textField(
                form, "Betrachtungsabstand (cm):", config.calibration().viewingDistanceCm());
        widthField = textField(form, "Monitorbreite (cm):", config.calibration().screenWidthCm());
        return form;
    }

    private JTextField textField(JPanel form, String label, double value) {
        form.add(new JLabel(label));
        JTextField field = new JTextField(COLUMNS);
        field.setText(String.format("%.0f", value));
        form.add(field);
        return field;
    }

    private void save() {
        try {
            double gap = Double.parseDouble(gapField.getText().trim());
            double distance = Double.parseDouble(distanceField.getText().trim());
            double width = Double.parseDouble(widthField.getText().trim());
            config.setGapCm(gap);
            config.setCalibration(new ScreenCalibration(distance, width));
            config.save();
            dispose();
        } catch (NumberFormatException e) {
            errorLabel.setText("Ungueltige Zahl.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
