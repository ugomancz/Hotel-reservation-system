package cz.muni.fi.pv168.hotel.gui.forms;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;

/**
 * @author Ondrej Kostik
 */
public class Settings {

    private final JDialog dialog;

    public Settings(JFrame frame) {
        dialog = new JDialog(frame, "Settings", ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
    }
}
