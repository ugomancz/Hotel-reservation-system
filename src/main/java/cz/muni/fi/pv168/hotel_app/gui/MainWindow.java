package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class MainWindow {

    public static Timetable timetable;
    public static JFrame frame;
    private static JPanel panel;

    public MainWindow(ReservationDao reservationDao) {
        frame = initFrame();
        panel = initPanel(reservationDao);
        frame.add(panel);
        frame.setVisible(true);
    }

    private JFrame initFrame() {
        frame = new JFrame("HotelApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        return frame;
    }

    private JPanel initPanel(ReservationDao reservationDao) {
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(Constants.BACKGROUND_COLOR);

        timetable = new Timetable(reservationDao);
        panel.add(new SidePanel(reservationDao), BorderLayout.EAST);
        panel.add(timetable, BorderLayout.CENTER);
        panel.add(new RoomNames(), BorderLayout.WEST);
        panel.add(new DayNames(LocalDate.now()), BorderLayout.NORTH);
        return panel;
    }

    private static class RoomNames extends JPanel {
        public Dimension dimensions = new Dimension(70, 500);

        private RoomNames() {
            super();
            setLayout(new GridLayout(Constants.NUMBER_OF_ROOMS, 1, 0, 1));
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

    static class DayNames extends JPanel {

        private static final JLabel[] labels = new JLabel[Constants.DAYS_IN_WEEK];

        private DayNames(LocalDate date) {
            super();
            setBorder(new EmptyBorder(0, 70, 0, SidePanel.dimension.width + 10));
            setBackground(Constants.BACKGROUND_COLOR);
            setLayout(new GridLayout(1, Constants.DAYS_IN_WEEK, 1, 0));

            initLabels();
            changeDates(date);
        }

        static void changeDates(LocalDate date) {
            LocalDate day = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
                String labelText = DateTimeFormatter.ofPattern("E dd.MM.").format(day); // e.g. Mon 23.11.
                labels[i].setText(labelText);
                day = day.plusDays(1);
            }
        }

        private void initLabels() {
            for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                labels[i] = label;
                add(label);
            }
        }
    }
}
