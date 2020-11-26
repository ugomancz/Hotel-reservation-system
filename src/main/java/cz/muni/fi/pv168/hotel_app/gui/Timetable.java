package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus.PAST;

public class Timetable extends JPanel {

    private static final JPanel[][] panels = new JPanel[Constants.NUMBER_OF_ROOMS][Constants.DAYS_IN_WEEK];

    public Timetable() {
        super();
        setLayout(new GridLayout(Constants.NUMBER_OF_ROOMS, Constants.DAYS_IN_WEEK, 0, 0));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Constants.BACKGROUND_COLOR);
        initPanels();
        drawWeek(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
    }

    private void initPanels() {
        for (int i = 0; i < Constants.NUMBER_OF_ROOMS; i++) {
            for (int j = 0; j < Constants.DAYS_IN_WEEK; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(new LineBorder(Color.black, 1));
                panels[i][j] = panel;
                add(panel);
            }
        }
    }

    private void setPanelName(JPanel panel, String name) {
        if (panel.getComponentCount() > 0)
            ((JLabel) panel.getComponent(0)).setText(name);
        else {
            panel.add(new JLabel(name, SwingConstants.CENTER));
        }
    }

    private void clearPanel(JPanel panel) {
        panel.setBackground(Color.white);
        if (panel.getComponentCount() > 0) {
            ((JLabel) panel.getComponent(0)).setText("");
        }
    }

    private boolean isInterfering(LocalDate newArrival, LocalDate newDeparture, LocalDate arrival, LocalDate departure) {
        return (newArrival.isAfter(arrival) && newArrival.isBefore(departure))
                || (newDeparture.isAfter(arrival) && newDeparture.isBefore(departure))
                || (newArrival.isBefore(arrival) && newDeparture.isAfter(departure))
                || (newArrival.isEqual(arrival) && newDeparture.isEqual(departure));
    }

    public boolean isFree(int room, LocalDate arrival, LocalDate departure) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getStatus() != PAST && reservation.getRoomNumber() == room
                    && isInterfering(arrival, departure, reservation.getArrival(), reservation.getDeparture())) {
                return false;
            }
        }
        return true;
    }

    private Reservation getReservation(int room, LocalDate date) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getRoomNumber() == room
                    && dateInReservation(date, reservation.getArrival(), reservation.getDeparture())) {
                return reservation;
            }
        }
        return null;
    }

    public int getNumOfReservations(LocalDate date) {
        int count = 0;
        for (Reservation reservation : Main.reservations) {
            if (reservation.getStatus() != PAST
                    && dateInReservation(date, reservation.getArrival(), reservation.getDeparture())) {
                count++;
            }
        }
        return count;
    }

    private boolean dateInReservation(LocalDate date, LocalDate arrival, LocalDate departure) {
        return date.isEqual(arrival) || date.isEqual(departure) || (date.isAfter(arrival) && date.isBefore(departure));
    }

    public void drawWeek(LocalDate arrival) {
        LocalDate monday = arrival.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int room = 0; room < Constants.NUMBER_OF_ROOMS; room++) { // for every room
            for (int day = 0; day < Constants.DAYS_IN_WEEK; day++) { // for every day of the week
                Reservation reservation = getReservation(room + 1, monday.plusDays(day));
                if (reservation != null) {
                    setPanelName(panels[room][day], reservation.getName());
                    panels[room][day].setBackground(reservation.getStatus().getColor());
                } else {
                    clearPanel(panels[room][day]);
                }
            }
        }
        revalidate();
    }
}
