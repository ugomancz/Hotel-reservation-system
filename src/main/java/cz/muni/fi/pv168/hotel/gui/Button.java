package cz.muni.fi.pv168.hotel.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {

    public static final Font FONT = new Font("Helvetica", Font.BOLD, 14);
    public static final Color BACKGROUND = new Color(240, 240, 240);

    public Button(String name) {
        super(name);
        setFocusPainted(false);
        setBackground(BACKGROUND);
        setFont(FONT);
    }

    public Button(String name, ActionListener listener) {
        this(name);
        addActionListener(listener);
    }
}
