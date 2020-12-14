package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalDate;

public class MainWindow {

    private final static I18N I18N = new I18N(MainWindow.class);
    private static JFrame frame;

    public MainWindow(ReservationDao reservationDao) {
        frame = new JFrame(I18N.getString("title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.add(initPanel(reservationDao));
        frame.setVisible(true);
    }

    private JPanel initPanel(ReservationDao reservationDao) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(Constants.BACKGROUND_COLOR);

        panel.add(new SidePanel(frame, reservationDao).getPanel(), BorderLayout.EAST);
        panel.add(new Timetable(reservationDao).getPanel(), BorderLayout.CENTER);
        panel.add(new RoomNames(), BorderLayout.WEST);
        panel.add(new TimetableHeader(LocalDate.now()).getPanel(), BorderLayout.NORTH);
        return panel;
    }

    private static class RoomNames extends JPanel {

        private RoomNames() {
            super();
            setLayout(new GridLayout(RoomDao.numberOfRooms(), 1, 0, 1));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setBackground(Constants.BACKGROUND_COLOR);
            setPreferredSize(new Dimension(75, 500));
            for (int i = 0; i < RoomDao.numberOfRooms(); i++) {
                JLabel label = new JLabel(I18N.getString("roomLabel") + (i + 1), SwingConstants.CENTER);
                label.setBackground(Constants.BACKGROUND_COLOR);
                add(label);
            }
        }
    }
}
