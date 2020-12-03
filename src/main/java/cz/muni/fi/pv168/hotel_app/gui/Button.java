package cz.muni.fi.pv168.hotel_app.gui;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    public static final Font font = new Font("Helvetica", Font.BOLD, 14);
    public static final Color background = new Color(240, 240, 240);

    public Button(String name) {
        super(name);
        setFocusPainted(false);
        setBackground(background);
        setFont(font);
    }
}
