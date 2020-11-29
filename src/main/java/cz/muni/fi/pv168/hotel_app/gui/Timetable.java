package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Timetable extends JPanel {

    private static final JPanel[][] panels = new JPanel[Constants.NUMBER_OF_ROOMS][Constants.DAYS_IN_WEEK];
    private static final JLabel[][] labels = new JLabel[Constants.NUMBER_OF_ROOMS][Constants.DAYS_IN_WEEK];
    private final ReservationDao reservationDao;

    public Timetable(ReservationDao reservationDao) {
        super();
        setLayout(new GridLayout(Constants.NUMBER_OF_ROOMS, Constants.DAYS_IN_WEEK, 0, 0));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Constants.BACKGROUND_COLOR);
        this.reservationDao = reservationDao;
        initPanels();
        drawWeek(LocalDate.now());
    }

    private void initPanels() {
        for (int i = 0; i < Constants.NUMBER_OF_ROOMS; i++) {
            for (int j = 0; j < Constants.DAYS_IN_WEEK; j++) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("", SwingConstants.CENTER);
                panel.setBorder(new LineBorder(Color.black, 1));
                panel.add(label);
                panels[i][j] = panel;
                labels[i][j] = label;
                add(panel);
            }
        }
    }

    private void clearPanel(int room, int day) {
        labels[room][day].setText("");
        panels[room][day].setBackground(Color.white);
    }

    private void fillPanel(int room, int day, String name, ReservationStatus status) {
        labels[room][day].setText(name);
        panels[room][day].setBackground(status.getColor());
    }

    private Reservation getReservation(int room, LocalDate date) {
        for (Reservation reservation : reservationDao.findAll()) {
            if (reservation.getRoomNumber() == room
                    && dateInReservation(date, reservation.getArrival(), reservation.getDeparture())) {
                return reservation;
            }
        }
        return null;
    }

    private boolean dateInReservation(LocalDate date, LocalDate arrival, LocalDate departure) {
        return date.isEqual(arrival) || date.isEqual(departure) || (date.isAfter(arrival) && date.isBefore(departure));
    }

    public void drawWeek(LocalDate date) {
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
}
