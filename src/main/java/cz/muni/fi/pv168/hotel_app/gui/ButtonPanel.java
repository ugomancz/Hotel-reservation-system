package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.gui.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonPanel extends JPanel {

    private static final int numOfButtons = 7;
    private final ReservationDao reservationDao;
    private Button newReservation, cancelReservation,
            checkIn, checkOut, roomInfo, reservationInfo, settings;

    public ButtonPanel(Dimension dimension, ReservationDao reservationDao) {
        super();
        setLayout(new GridLayout(numOfButtons, 1, 5, 5));
        setBackground(Constants.BACKGROUND_COLOR);
        setPreferredSize(dimension);
        this.reservationDao = reservationDao;

        addButtons();
    }

    private static void notImplemented() {
        JOptionPane.showMessageDialog(MainWindow.frame, "Not yet implemented");
    }

    private void addButtons() {
        newReservation = initButton("New reservation");
        cancelReservation = initButton("Cancel reservation");
        checkIn = initButton("Check in");
        checkOut = initButton("Check out");
        reservationInfo = initButton("Reservation info");
        roomInfo = initButton("Room info");
        settings = initButton("Settings");

        add(newReservation);
        add(cancelReservation);
        add(checkIn);
        add(checkOut);
        add(roomInfo);
        add(reservationInfo);
        add(settings);
    }

    private Button initButton(String name) {
        Button button = new Button(name);
        button.addActionListener(this::actionPerformed);
        return button;
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(newReservation)) {
            new NewReservation(reservationDao);
        } else if (e.getSource().equals(reservationInfo)) {
            new ReservationInfo(reservationDao);
        } else if (e.getSource().equals(cancelReservation)) {
            new CancelReservation();
        } else if (e.getSource().equals(checkIn)) {
            new CheckIn(reservationDao);
        } else if (e.getSource().equals(checkOut)) {
            new CheckOut(reservationDao);
        } else if (e.getSource().equals(roomInfo)) {
            notImplemented();
        } else if (e.getSource().equals(settings)) {
            notImplemented();
        }
    }
}
