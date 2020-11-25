package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow {

    public static Timetable timetable = new Timetable();
    public static JFrame frame;
    private static JPanel panel;

    public MainWindow() {
        frame = initFrame();
        panel = initPanel();
        frame.add(panel);
        frame.setVisible(true);
    }

    private JFrame initFrame() {
        frame = new JFrame("HotelApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        return frame;
    }

    private JPanel initPanel() {
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(5,5));
        panel.setBackground(Constants.BACKGROUND_COLOR);

        panel.add(new SidePanel(), BorderLayout.EAST);
        panel.add(timetable, BorderLayout.CENTER);
        panel.add(new RoomNames(), BorderLayout.WEST);
        panel.add(new DayNames(), BorderLayout.NORTH);
        return panel;
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
