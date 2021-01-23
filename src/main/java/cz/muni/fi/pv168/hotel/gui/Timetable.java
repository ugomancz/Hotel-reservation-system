package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Timetable {

    private static final Color FIRST_DAY_OF_RESERVATION = new Color(60, 160, 50);
    private static final Map<ReservationStatus, Color> STATUS_COLOR = Map.of(
            ReservationStatus.PLANNED, new Color(13, 218, 13),
            ReservationStatus.ONGOING, Color.orange,
            ReservationStatus.PAST, Color.lightGray
    );
    private static final Map<Integer, Integer> ROOM_INDEX = Map.ofEntries(
            Map.entry(101, 0),
            Map.entry(102, 1),
            Map.entry(103, 2),
            Map.entry(104, 3),
            Map.entry(105, 4),
            Map.entry(106, 5),
            Map.entry(201, 6),
            Map.entry(202, 7),
            Map.entry(203, 8),
            Map.entry(204, 9),
            Map.entry(205, 10),
            Map.entry(301, 11),
            Map.entry(302, 12),
            Map.entry(303, 13)
    );
    private static RoomDao roomDao;
    private static JTextPane[][] TEXT_PANES;
    private static ReservationDao reservationDao;
    private static LocalDate displayedMonday;
    private final JPanel panel;

    Timetable(ReservationDao reservationDao, RoomDao roomDao) {
        Timetable.roomDao = roomDao;
        TEXT_PANES = new JTextPane[roomDao.numberOfRooms()][Constants.DAYS_IN_WEEK];
        panel = new JPanel();
        panel.setLayout(new GridLayout(roomDao.numberOfRooms(), Constants.DAYS_IN_WEEK, 0, 0));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        Timetable.reservationDao = reservationDao;
        initPanes();
        drawWeek(LocalDate.now());
    }

    private static void clearPane(int room, int day) {
        TEXT_PANES[room][day].setText("");
        TEXT_PANES[room][day].setBackground(Color.white);
    }

    private static void fillPane(int room, int day, Reservation reservation) {
        if (reservation.getArrival().isEqual(displayedMonday.plusDays(day)) && reservation.getStatus() != ReservationStatus.PAST) {
            TEXT_PANES[room][day].setBackground(FIRST_DAY_OF_RESERVATION);
        } else {
            TEXT_PANES[room][day].setBackground(STATUS_COLOR.get(reservation.getStatus()));
        }
        TEXT_PANES[room][day].setText(reservation.getName());

    }

    private static void fillPaneWithTwo(int room, int day, Reservation reservationOne, Reservation reservationTwo) {
        if (reservationOne.getArrival().isBefore(reservationTwo.getArrival())) {
            TEXT_PANES[room][day].setText(reservationOne.getName() + " / " + reservationTwo.getName());
        } else {
            TEXT_PANES[room][day].setText(reservationTwo.getName() + " / " + reservationOne.getName());
        }
        if (reservationOne.getStatus() == ReservationStatus.PAST
                && reservationTwo.getStatus() == ReservationStatus.PAST) {
            TEXT_PANES[room][day].setBackground(STATUS_COLOR.get(reservationOne.getStatus()));
        } else {
            TEXT_PANES[room][day].setBackground(FIRST_DAY_OF_RESERVATION);
        }
    }

    private static void updatePane(List<Reservation> reservations, int room, int day) {
        if (reservations.size() == 0) {
            clearPane(room, day);
        } else if (reservations.size() == 1) {
            Reservation reservation = reservations.get(0);
            fillPane(room, day, reservation);
        } else {
            Reservation reservationOne = reservations.get(0);
            Reservation reservationTwo = reservations.get(1);
            fillPaneWithTwo(room, day, reservationOne, reservationTwo);
        }
    }

    public static void drawWeek(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        displayedMonday = monday;
        new UpdateTable(monday).execute();
    }

    public static void refresh() {
        drawWeek(displayedMonday);
    }

    JPanel getPanel() {
        return panel;
    }

    private void initPanes() {
        for (int i = 0; i < roomDao.numberOfRooms(); i++) {
            for (int j = 0; j < Constants.DAYS_IN_WEEK; j++) {
                JTextPane textPane = new JTextPane();
                StyledDocument styledDocument = textPane.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                styledDocument.setParagraphAttributes(0, styledDocument.getLength(), center, false);

                textPane.setFont(new Font("Helvetica", Font.BOLD, 12));
                textPane.setEditable(false);
                textPane.setBorder(new LineBorder(Color.black, 1));
                TEXT_PANES[i][j] = textPane;
                panel.add(textPane);
            }
        }
    }

    private static class UpdateTable extends SwingWorker<List<List<Reservation>>, Void> {

        LocalDate monday;

        public UpdateTable(LocalDate monday) {
            this.monday = monday;
        }

        @Override
        protected List<List<Reservation>> doInBackground() {
            List<List<Reservation>> reservations = new ArrayList<>();

            Map<Integer, Integer> reversedRoomIndex = ROOM_INDEX.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            for (int room = 0; room < roomDao.numberOfRooms(); room++) { // for every room
                for (int day = 0; day < Constants.DAYS_IN_WEEK; day++) { // for every day of the week
                    reservations.add(reservationDao.getReservation(reversedRoomIndex.get(room), monday.plusDays(day)));
                }
            }
            return reservations;
        }

        @Override
        protected void done() {
            try {
                List<List<Reservation>> reservations = get();
                for (int i = 0; i < reservations.size(); i++) {
                    updatePane(reservations.get(i), i / 7, i % 7);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
