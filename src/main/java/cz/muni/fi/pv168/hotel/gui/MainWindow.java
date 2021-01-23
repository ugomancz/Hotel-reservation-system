package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.forms.ErrorDialog;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public class MainWindow {

    private static final I18N I18N = new I18N(MainWindow.class);
    static final Dimension sidePanelDimension = new Dimension(255, 30);
    private static JFrame frame;
    private final RoomDao roomDao;
    private final JPanel panel = new JPanel();

    public MainWindow(ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        this.roomDao = roomDao;
        frame = new JFrame(I18N.getString("windowTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.add(initMainPanel(reservationDao, guestDao, roomDao));
        frame.setVisible(true);
    }

    public static void run(ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        try {
            new MainWindow(reservationDao, guestDao, roomDao);
        } catch (Exception ex) {
            new ErrorDialog(null, I18N.getString("initError"));
            ex.printStackTrace();
            System.exit(-1);
        }
    }


    private JPanel initMainPanel(ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(Constants.BACKGROUND_COLOR);

        panel.add(initSidePanel(reservationDao, guestDao, roomDao), BorderLayout.EAST);
        panel.add(new Timetable(reservationDao, roomDao).getPanel(), BorderLayout.CENTER);
        new LoadRooms().execute();
        panel.add(new TimetableHeader(LocalDate.now()).getPanel(), BorderLayout.NORTH);
        return panel;
    }

    private JPanel initSidePanel(ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(Constants.BACKGROUND_COLOR);
        sidePanel.setLayout(new BorderLayout(0, 10));
        sidePanel.setPreferredSize(sidePanelDimension);

        sidePanel.add(new ButtonPanel(frame, reservationDao, guestDao, roomDao).getPanel(), BorderLayout.CENTER);
        sidePanel.add(new DesignedCalendar(reservationDao, roomDao).getCalendar(), BorderLayout.SOUTH);
        return sidePanel;
    }

    private static class RoomNames extends JPanel {

        private RoomNames(List<Room> rooms) {
            super();
            setLayout(new GridLayout(rooms.size(), 1, 0, 1));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setBackground(Constants.BACKGROUND_COLOR);
            setPreferredSize(new Dimension(80, 500));
            for (Room room : rooms) {
                JLabel label = new JLabel(I18N.getString("roomLabel") + " " + room.getRoomNumber(), SwingConstants.CENTER);
                label.setBackground(Constants.BACKGROUND_COLOR);
                add(label);
            }
        }
    }

    private class LoadRooms extends SwingWorker<List<Room>, Void> {

        @Override
        protected List<Room> doInBackground() {
            return roomDao.findAll();
        }

        @Override
        protected void done() {
            try {
                panel.add(new RoomNames(get()), BorderLayout.WEST);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
