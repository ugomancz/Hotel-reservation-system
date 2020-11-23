package cz.muni.fi.pv168.project.gui;

import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    public static Timetable timetable = new Timetable();

    public MainPanel() {
        super(new BorderLayout(10, 10));
        this.setBackground(Main.BACKGROUND_COLOR);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.add(new SidePanel(), BorderLayout.EAST);
        this.add(timetable, BorderLayout.CENTER);
        this.add(new RoomNames(), BorderLayout.WEST);
        this.add(new DayNames(), BorderLayout.NORTH);
    }

    private static class RoomNames extends JPanel {
        public Dimension dimensions = new Dimension(60, 500);

        private RoomNames() {
            super();
            this.setLayout(new GridLayout(15, 1, 0, 1));
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.setBackground(Main.BACKGROUND_COLOR);
            this.setPreferredSize(dimensions);
            for (int i = 0; i < Main.NUMBER_OF_ROOMS; i++) {
                JLabel label = new JLabel("Room n." + (i + 1));
                label.setBackground(Main.BACKGROUND_COLOR);
                this.add(label);
            }

        }
    }

    private static class DayNames extends JPanel {
        private static final String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday"};

        private DayNames() {
            super();
            this.setBorder(new EmptyBorder(0, 70, 0, Button.dimension.width + 20));
            this.setBackground(Main.BACKGROUND_COLOR);
            this.setLayout(new GridLayout(1, Main.DAYS_IN_WEEK, 1, 0));
            for (int i = 0; i < Main.DAYS_IN_WEEK; i++) {
                JLabel label = new JLabel(dayNames[i], SwingConstants.CENTER);
                this.add(label);
            }
        }
    }
}
