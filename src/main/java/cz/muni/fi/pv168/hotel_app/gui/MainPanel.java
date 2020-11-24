package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    public static Timetable timetable = new Timetable();

    public MainPanel() {
        super(new BorderLayout(10, 10));
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(new SidePanel(), BorderLayout.EAST);
        add(timetable, BorderLayout.CENTER);
        add(new RoomNames(), BorderLayout.WEST);
        add(new DayNames(), BorderLayout.NORTH);
    }

    private static class RoomNames extends JPanel {
        public Dimension dimensions = new Dimension(60, 500);

        private RoomNames() {
            super();
            setLayout(new GridLayout(15, 1, 0, 1));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setBackground(Constants.BACKGROUND_COLOR);
            setPreferredSize(dimensions);
            for (int i = 0; i < Constants.NUMBER_OF_ROOMS; i++) {
                JLabel label = new JLabel("Room n." + (i + 1));
                label.setBackground(Constants.BACKGROUND_COLOR);
                add(label);
            }

        }
    }

    private static class DayNames extends JPanel {
        private static final String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday"};

        private DayNames() {
            super();
            setBorder(new EmptyBorder(0, 70, 0, Button.dimension.width + 20));
            setBackground(Constants.BACKGROUND_COLOR);
            setLayout(new GridLayout(1, Constants.DAYS_IN_WEEK, 1, 0));
            for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
                JLabel label = new JLabel(dayNames[i], SwingConstants.CENTER);
                add(label);
            }
        }
    }
}
