package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.data.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JPanel;
import javax.swing.JTextPane;
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
import java.util.List;
import java.util.Map;

public class Timetable {

    private static final JTextPane[][] TEXT_PANES = new JTextPane[RoomDao.numberOfRooms()][Constants.DAYS_IN_WEEK];
    private static final Color FIRST_DAY_OF_RESERVATION = new Color(60, 160, 50);
    private static final Map<ReservationStatus, Color> STATUS_COLOR = Map.of(
            ReservationStatus.PLANNED, new Color(13, 218, 13),
            ReservationStatus.ONGOING, Color.orange,
            ReservationStatus.PAST, Color.lightGray
    );
    private static ReservationDao reservationDao;
    private final JPanel panel;
    private static final Map<ReservationStatus, Color> STATUS_COLOR = Map.of(
            ReservationStatus.PLANNED, Constants.PLANNED_RESERVATION,
            ReservationStatus.ONGOING, Constants.ONGOING_RESERVATION,
            ReservationStatus.PAST, Constants.PAST_RESERVATION
            );

    Timetable(ReservationDao reservationDao) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(RoomDao.numberOfRooms(), Constants.DAYS_IN_WEEK, 0, 0));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        Timetable.reservationDao = reservationDao;
        initPanels();
        drawWeek(LocalDate.now());
    }

    private static void clearPane(int room, int day) {
        TEXT_PANES[room][day].setText("");
        TEXT_PANES[room][day].setBackground(Color.white);
    }

    private static void fillPane(int room, int day, LocalDate today, Reservation reservation) {
        if (reservation.getArrival().isEqual(today) && reservation.getStatus() != ReservationStatus.PAST) {
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

    private static void updatePane(List<Reservation> reservations, LocalDate today, int room, int day) {
        if (reservations.size() == 0) {
            clearPane(room, day);
        } else if (reservations.size() == 1) {
            Reservation reservation = reservations.get(0);
            fillPane(room, day, today, reservation);
        } else {
            Reservation reservationOne = reservations.get(0);
            Reservation reservationTwo = reservations.get(1);
            fillPaneWithTwo(room, day, reservationOne, reservationTwo);
        }
    }

    public static void drawWeek(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int room = 0; room < RoomDao.numberOfRooms(); room++) { // for every room
            for (int day = 0; day < Constants.DAYS_IN_WEEK; day++) { // for every day of the week
                LocalDate currentDay = monday.plusDays(day);
                updatePane(reservationDao.getReservation(room + 1, currentDay), currentDay, room, day);
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void initPanels() {
        for (int i = 0; i < RoomDao.numberOfRooms(); i++) {
            for (int j = 0; j < Constants.DAYS_IN_WEEK; j++) {
                JTextPane textPane = new JTextPane();
                StyledDocument styledDocument = textPane.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                styledDocument.setParagraphAttributes(0, styledDocument.getLength(), center, false);

                textPane.setFont(new Font("Helvetica", Font.BOLD, 13));
                textPane.setEditable(false);
                textPane.setBorder(new LineBorder(Color.black, 1));
                TEXT_PANES[i][j] = textPane;
                panel.add(textPane);
            }
        }
    }
}
