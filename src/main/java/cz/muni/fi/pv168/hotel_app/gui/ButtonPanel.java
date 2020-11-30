package cz.muni.fi.pv168.hotel_app.gui;

import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.gui.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonPanel extends JPanel {

    private static final int numOfButtons = 8;
    private Button newReservation, changeReservation, cancelReservation,
            checkIn, checkOut, roomInfo, reservationInfo, settings;

    public ButtonPanel(Dimension dimension) {
        super();
        setLayout(new GridLayout(numOfButtons, 1, 5, 5));
        setBackground(Constants.BACKGROUND_COLOR);
        setPreferredSize(dimension);

        addButtons();
    }

    private static void notImplemented() {
        JOptionPane.showInternalMessageDialog(null, "Not yet implemented");
    }

    private void addButtons() {
        newReservation = initButton("New reservation");
        changeReservation = initButton("Change reservation");
        cancelReservation = initButton("Cancel reservation");
        checkIn = initButton("Check in");
        checkOut = initButton("Check out");
        roomInfo = initButton("Room info");
        reservationInfo = initButton("Reservation info");
        settings = initButton("Settings");

        add(newReservation);
        add(changeReservation);
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
            new NewReservation();
        } else if (e.getSource().equals(changeReservation)) {
            new ChangeReservation();
        } else if (e.getSource().equals(cancelReservation)) {
            new CancelReservation();
        } else if (e.getSource().equals(checkIn)) {
            new CheckIn();
        } else if (e.getSource().equals(checkOut)) {
            new CheckOut();
        } else if (e.getSource().equals(roomInfo)) {
            notImplemented();
        } else if (e.getSource().equals(reservationInfo)) {
            notImplemented();
        } else if (e.getSource().equals(settings)) {
            notImplemented();
        }
    }
}
