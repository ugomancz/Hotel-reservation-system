package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.gui.forms.CancelReservation;
import cz.muni.fi.pv168.hotel.gui.forms.CheckIn;
import cz.muni.fi.pv168.hotel.gui.forms.CheckOut;
import cz.muni.fi.pv168.hotel.gui.forms.NewReservation;
import cz.muni.fi.pv168.hotel.gui.forms.ReservationInfo;
import cz.muni.fi.pv168.hotel.gui.forms.RoomInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public class ButtonPanel {

    private static final int numOfButtons = 7;
    private final JPanel panel;
    private final ReservationDao reservationDao;

    public ButtonPanel(JFrame frame, ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        panel = new JPanel();
        panel.setLayout(new GridLayout(numOfButtons, 1, 5, 5));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        addButtons(frame);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void addButtons(JFrame frame) {
        panel.add(initButton("New reservation", e -> new NewReservation(frame, reservationDao)));
        panel.add(initButton("Cancel reservation", e -> new CancelReservation(frame, reservationDao)));
        panel.add(initButton("Check in", e -> new CheckIn(frame, reservationDao)));
        panel.add(initButton("Check out", e -> new CheckOut(frame, reservationDao)));
        panel.add(initButton("Reservation info", e -> new ReservationInfo(frame, reservationDao)));
        panel.add(initButton("Room info", e -> new RoomInfo(frame)));
        panel.add(new Button("Settings"));
    }

    private Button initButton(String label, ActionListener listener) {
        Button button = new Button(label);
        button.addActionListener(listener);
        return button;
    }
}
