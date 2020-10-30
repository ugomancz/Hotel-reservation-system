package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Timetable extends JPanel {
    public Timetable() {
        super();
        this.setBackground(Color.white);
        this.setLayout(new GridLayout(10,31));
        this.setBorder(new EmptyBorder(0,0,0,0));

        for (int i =0; i<(310); i++){
            final JLabel label = new JLabel();
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(label);
        }
    }
}
