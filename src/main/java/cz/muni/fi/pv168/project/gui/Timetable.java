package cz.muni.fi.pv168.project.gui;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.reservations.Reservation;
import cz.muni.fi.pv168.project.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static cz.muni.fi.pv168.project.reservations.ReservationStatus.past;

public class Timetable extends JPanel {

    static JPanel[][] panels = new JPanel[Main.numberOfRooms][Main.week];
    public LocalDate selectedDate;

    public Timetable() {
        super();
        this.setLayout(new GridLayout(Main.numberOfRooms, Main.week, 0, 0));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBackground(Main.backgroundColor);
        this.initPanels(Main.numberOfRooms, Main.week);
        selectedDate = LocalDate.now();
        this.drawWeek(selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
    }

    private void initPanels(int y, int x) {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(new LineBorder(Color.black, 1));
                panels[i][j] = panel;
                this.add(panel);
            }
        }
    }

    private void setPanelColor(JPanel panel, ReservationStatus status) {
        switch (status) {
            case planned:
                panel.setBackground(Color.green);
                return;
            case ongoing:
                panel.setBackground(Color.orange);
                return;
            case past:
                panel.setBackground(Color.darkGray);
        }
    }

    private void setPanelName(JPanel panel, String name) {
        if (panel.getComponentCount() > 0)
            ((JLabel) panel.getComponent(0)).setText(name);
        else {
            panel.add(new JLabel(name, SwingConstants.CENTER));
        }
    }

    private boolean isInterfering(LocalDate newArrival, LocalDate newDeparture, LocalDate arrival, LocalDate departure) {
        return (newArrival.isAfter(arrival) && newArrival.isBefore(departure))
                || (newDeparture.isAfter(arrival) && newDeparture.isBefore(departure))
                || (newArrival.isBefore(arrival) && newDeparture.isAfter(departure));
    }

    public boolean isFree(int room, LocalDate arrival, LocalDate departure) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getStatus() != past && reservation.getRoomNumber() == room
                    && isInterfering(arrival, departure, reservation.getArrival(), reservation.getDeparture())) {
                return false;
            }
        }
        return true;
    }

    private Reservation getReservation(int room, LocalDate date) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getStatus() != past && reservation.getRoomNumber() == room
                    && dateInReservation(date, reservation.getArrival(), reservation.getDeparture())) {
                return reservation;
            }
        }
        return null;
    }

    private boolean dateInReservation(LocalDate date, LocalDate arrival, LocalDate departure) {
        return date.isEqual(arrival) || date.isEqual(departure) || (date.isAfter(arrival) && date.isBefore(departure));
    }

    public void drawWeek(LocalDate arrival) {
        LocalDate monday = arrival.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int room = 0; room < Main.numberOfRooms; room++) { // for every room
            for (int day = 0; day < Main.week; day++) { // for every day of the week
                Reservation reservation = getReservation(room+1, monday.plusDays(day));
                if (reservation != null) {
                    setPanelName(panels[room][day], reservation.getName());
                    setPanelColor(panels[room][day], reservation.getStatus());
                }
            }
        }
        this.revalidate();
    }
}
