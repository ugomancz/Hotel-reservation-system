package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;

/**
 * @author Ondrej Kostik
 */
class ErrorDialog {

    private final JDialog dialog;

    ErrorDialog(Window owner, String message) {
        dialog = new JDialog(owner, "Error", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(owner);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        initLayout(message);
        dialog.setVisible(true);
    }

    private void initLayout(String message) {
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(200, 100));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Button button = new Button("OK", (e) -> dialog.dispose());
        button.setPreferredSize(new Dimension(50, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.getRootPane().setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.add(messageLabel);
        dialog.add(button);
        dialog.setSize(240, 130);
        dialog.setResizable(false);
    }
}
