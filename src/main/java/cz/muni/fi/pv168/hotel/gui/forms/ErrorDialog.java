package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;

/**
 * @author Ondrej Kostik
 */
class ErrorDialog {

    private static final I18N I18N = new I18N(ErrorDialog.class);
    private final JDialog dialog;

    ErrorDialog(Window owner, String message) {
        dialog = new JDialog(owner, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(owner);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        initLayout(message);
        dialog.setSize(240, 130);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void initLayout(String message) {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDocument = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        styledDocument.setParagraphAttributes(0, styledDocument.getLength(), center, false);
        textPane.setEditable(false);
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.setPreferredSize(new Dimension(200, 100));
        textPane.setBackground(new Color(240, 240, 240));
        textPane.setText(message);

        Button button = new Button("OK", (e) -> dialog.dispose());
        button.setPreferredSize(new Dimension(50, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.getRootPane().setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.add(textPane);
        dialog.add(button);
    }
}
