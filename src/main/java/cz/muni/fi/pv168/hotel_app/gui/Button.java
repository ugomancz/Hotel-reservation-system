package cz.muni.fi.pv168.hotel_app.gui;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    final static Dimension dimension = new Dimension(220, 30);

    public Button(String name) {
        super(name);
        setFocusPainted(false);
        setBackground(new Color(240,240,240));
        setFont(new Font("Tahoma", Font.BOLD, 14));
    }
}
