package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.data.ReservationDao;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class MainWindow {

    private static JFrame frame;
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

        panel.add(new SidePanel(frame, reservationDao).getPanel(), BorderLayout.EAST);
        panel.add(new Timetable(reservationDao).getPanel(), BorderLayout.CENTER);
        panel.add(new RoomNames(), BorderLayout.WEST);
        panel.add(new DayNames(LocalDate.now()), BorderLayout.NORTH);
        return panel;
    }

    private static class RoomNames extends JPanel {

        public Dimension dimensions = new Dimension(75, 500);

        private RoomNames() {
            super();
            setLayout(new GridLayout(RoomDao.numberOfRooms(), 1, 0, 1));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setBackground(Constants.BACKGROUND_COLOR);
            setPreferredSize(dimensions);
            for (int i = 0; i < RoomDao.numberOfRooms(); i++) {
                JLabel label = new JLabel("Room n." + (i + 1), SwingConstants.CENTER);
                label.setBackground(Constants.BACKGROUND_COLOR);
                add(label);
            }
        }
    }

    static class DayNames extends JPanel {

        private static final JLabel[] labels = new JLabel[Constants.DAYS_IN_WEEK];
        private static LocalDate current;
        private Button previous, next;

        private DayNames(LocalDate date) {
            super();
            current = date;
            setBorder(new EmptyBorder(0, 75, 0, 0));
            setBackground(Constants.BACKGROUND_COLOR);
            setLayout(new BorderLayout(5, 0));

            initDayNames();
            changeDates(current);
        }

        static void changeDates(LocalDate date) {
            LocalDate day = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            current = day;
            for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
                String labelText = DateTimeFormatter.ofPattern("E dd.MM.").format(day); // e.g. Mon 23.11.
                if (day.isEqual(LocalDate.now())) { // current date gets highlighted
                    labels[i].setText(String.format("<html><u>%s</u></html>", labelText));
                } else {
                    labels[i].setText(labelText);
                }
                day = day.plusDays(1);
            }
        }

        private void initLabels(JPanel panel) {
            for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                labels[i] = label;
                panel.add(label);
            }
        }

        private void initButtons(JPanel buttons) {
            previous = new Button("<<");
            previous.addActionListener(this::actionPerformed);
            buttons.add(previous);

            next = new Button(">>");
            next.addActionListener(this::actionPerformed);
            buttons.add(next);
        }

        private void initDayNames() {
            JPanel dayNames = new JPanel();
            dayNames.setBackground(Constants.BACKGROUND_COLOR);
            dayNames.setLayout(new GridLayout(1, Constants.DAYS_IN_WEEK));
            initLabels(dayNames);
            add(dayNames, BorderLayout.CENTER);

            JPanel buttons = new JPanel();
            buttons.setBackground(Constants.BACKGROUND_COLOR);
            buttons.setPreferredSize(new Dimension(SidePanel.dimension.width, 20));
            buttons.setLayout(new GridLayout(1, 2, 5, 0));
            initButtons(buttons);
            add(buttons, BorderLayout.EAST);
        }

        private void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(previous)) {
                current = current.minusWeeks(1);
                changeDates(current);
                Timetable.drawWeek(current);
                DesignedCalendar.setDate(current);
            } else if (e.getSource().equals(next)) {
                current = current.plusWeeks(1);
                changeDates(current);
                Timetable.drawWeek(current);
                DesignedCalendar.setDate(current);
            }
        }
    }
}
