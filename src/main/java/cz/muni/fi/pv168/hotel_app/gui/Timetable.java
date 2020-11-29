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

    private static void clearPanel(int room, int day) {
        TEXT_PANES[room][day].setText("");
        TEXT_PANES[room][day].setBackground(Color.white);
    }

    private static void fillPanel(int room, int day, String name, ReservationStatus status) {
        TEXT_PANES[room][day].setText(name);
        TEXT_PANES[room][day].setBackground(status.getColor());
    }

    private static Reservation getReservation(int room, LocalDate date) {
        for (Reservation reservation : reservationDao.findAll()) {
            if (reservation.getRoomNumber() == room
                    && dateInReservation(date, reservation.getArrival(), reservation.getDeparture())) {
                return reservation;
            }
        }
        return null;
    }

    private static boolean dateInReservation(LocalDate date, LocalDate arrival, LocalDate departure) {
        return date.isEqual(arrival) || date.isEqual(departure) || (date.isAfter(arrival) && date.isBefore(departure));
    }

    public static void drawWeek(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int room = 0; room < Constants.NUMBER_OF_ROOMS; room++) { // for every room
            for (int day = 0; day < Constants.DAYS_IN_WEEK; day++) { // for every day of the week
                Reservation reservation = getReservation(room + 1, monday.plusDays(day));
                if (reservation != null) {
                    fillPanel(room, day, reservation.getName(), reservation.getStatus());
                } else {
                    clearPanel(room, day);
                }
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
