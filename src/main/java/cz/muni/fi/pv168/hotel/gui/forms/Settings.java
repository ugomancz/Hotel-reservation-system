package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;

/**
 * @author Ondrej Kostik
 */
public class Settings {

    private static final I18N I18N = new I18N(Settings.class);
    private final JDialog dialog;

    public Settings(JFrame frame) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
    }
}
