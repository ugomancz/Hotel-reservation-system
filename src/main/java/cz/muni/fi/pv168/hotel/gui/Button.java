package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class Button extends JButton {

    public Button(String name) {
        super(name);
        setFocusPainted(false);
        setBackground(Constants.BUTTON_BACKGROUND);
        setFont(Constants.BUTTON_FONT);
    }

    public Button(String name, ActionListener listener) {
        this(name);
        addActionListener(listener);
    }
}
