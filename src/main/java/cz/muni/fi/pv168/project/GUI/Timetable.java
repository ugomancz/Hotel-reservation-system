package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Timetable extends JPanel {
    public Timetable() {
        super();
        this.setLayout(new GridLayout(15,7,0,0));
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setBackground(MainPanel.mainBackground);
        for (int i = 0; i < 15*7; i++) {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(i*2,i*2,i*2));
            panel.setBorder(new LineBorder(Color.black,1));
            this.add(panel);
        }
    }

    public void changeColor(Color color) {
        return;
    }
}
