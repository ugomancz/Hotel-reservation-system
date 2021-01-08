package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Validation;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 * @author Ondrej Kostik
 */
public class Settings {

    private static final I18N I18N = new I18N(Settings.class);
    private final JDialog dialog;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final File configFile = new File(Constants.CONFIG_FILE_PATH);
    private Properties properties;
    private JTextField textField;

    public Settings(JFrame frame) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.getRootPane().registerKeyboardAction((e) -> new StoreProperties().execute(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        new LoadProperties().execute();
        initLayout();
        dialog.setVisible(true);
    }

    private void initLayout() {
        dialog.setMinimumSize(new Dimension(250, 130));
        dialog.setLayout(new GridBagLayout());
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel(I18N.getString("feesLabel"));
        textField = new JTextField(10);
        textField.setText(properties.getProperty("localFee"));
        Button okButton = new Button(I18N.getString("confirmButton"), (e) -> updateProperty());
        Button cancelButton = new Button(I18N.getString("cancelButton"), (e) -> dialog.dispose());

        addComponent(label, 0, 0);
        addComponent(textField, 1, 0);
        addComponent(okButton, 0, 1);
        gbc.anchor = GridBagConstraints.LINE_END;
        addComponent(cancelButton, 1, 1);
    }

    private void addComponent(Component component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void updateProperty() {
        if (Validation.isNotNumeric(textField.getText())) {
            new ErrorDialog(dialog, I18N.getString("numberError"));
        } else {
            new StoreProperties().execute();
        }
    }

    private class LoadProperties extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            Properties defaultProperties = new Properties();
            defaultProperties.setProperty("localFee", "50");
            properties = new Properties(defaultProperties);

            try (InputStream inputStream = new FileInputStream(configFile)) {
                properties.load(inputStream);
            }
            return properties.getProperty("localFee");
        }

        @Override
        public void done() {
            try {
                textField.setText(get());
            } catch (Exception ex) {
                if (configFile.exists()) {
                    new ErrorDialog(dialog, I18N.getString("fileError"));
                }
            }
        }
    }

    private class StoreProperties extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {
            properties.setProperty("localFee", textField.getText());
            try {
                try (OutputStream out = new FileOutputStream(configFile)) {
                    properties.store(out, "property updated");
                }
            } catch (IOException ex) {
                new ErrorDialog(dialog, I18N.getString("fileError"));
            }
            dialog.dispose();
            return null;
        }

        @Override
        public void done() {
            dialog.dispose();
        }
    }
}
