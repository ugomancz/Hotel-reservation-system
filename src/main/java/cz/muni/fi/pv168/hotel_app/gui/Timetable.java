package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class Timetable extends JPanel {

    private static final JTextPane[][] TEXT_PANES = new JTextPane[Constants.NUMBER_OF_ROOMS][Constants.DAYS_IN_WEEK];
    private static ReservationDao reservationDao;

    public Timetable(ReservationDao reservationDao) {
        super();
        setLayout(new GridLayout(Constants.NUMBER_OF_ROOMS, Constants.DAYS_IN_WEEK, 0, 0));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Constants.BACKGROUND_COLOR);
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
            TEXT_PANES[room][day].setBackground(Constants.FIRST_DAY_OF_RESERVATION);
        } else {
            TEXT_PANES[room][day].setBackground(reservation.getStatus().getColor());
        }
        TEXT_PANES[room][day].setText(reservation.getName());

    }

    private static void fillPaneWithTwo(int room, int day, Reservation reservationOne, Reservation reservationTwo) {
        if (reservationOne.getArrival().isBefore(reservationTwo.getArrival())) {
            TEXT_PANES[room][day].setText(reservationOne.getName() + " / " + reservationTwo.getName());
        } else {
            TEXT_PANES[room][day].setText(reservationTwo.getName() + " / " + reservationOne.getName());
        }
        TEXT_PANES[room][day].setBackground(Constants.FIRST_DAY_OF_RESERVATION);
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
        for (int room = 0; room < Constants.NUMBER_OF_ROOMS; room++) { // for every room
            for (int day = 0; day < Constants.DAYS_IN_WEEK; day++) { // for every day of the week
                LocalDate currentDay = monday.plusDays(day);
                updatePane(reservationDao.getReservation(room + 1, currentDay), currentDay, room, day);
            }
        }
    }

    private void initPanels() {
        for (int i = 0; i < Constants.NUMBER_OF_ROOMS; i++) {
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
                add(textPane);
            }
        }
    }
}
