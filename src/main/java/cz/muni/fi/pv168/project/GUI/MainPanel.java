package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    final static Color mainBackground = Color.lightGray;

    public MainPanel() {
        super(new BorderLayout(10, 10));
        this.setBackground(mainBackground);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        //JPanel calendar = new JPanel();
        this.add(new ButtonPanel(), BorderLayout.EAST);
        this.add(new CalendarPanel(), BorderLayout.NORTH);
        this.add(new Timetable(), BorderLayout.CENTER);
    }
}
