package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    final static Dimension dimension = new Dimension(220, 30);

    public Button(String name) {
        super(name);
        this.setFocusPainted(false);
        this.setBackground(new Color(237, 235, 235));
        this.setFont(new Font("Tahoma", Font.BOLD, 14));
    }
}
