package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.forms.CancelReservation;
import cz.muni.fi.pv168.hotel.gui.forms.CheckIn;
import cz.muni.fi.pv168.hotel.gui.forms.CheckOut;
import cz.muni.fi.pv168.hotel.gui.forms.NewReservation;
import cz.muni.fi.pv168.hotel.gui.forms.ReservationInfo;
import cz.muni.fi.pv168.hotel.gui.forms.RoomInfo;
import cz.muni.fi.pv168.hotel.gui.forms.Settings;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;

class ButtonPanel {

    private static final int NUM_OF_BUTTONS = 7;
    private static final I18N I18N = new I18N(ButtonPanel.class);
    private final JPanel panel;
    private final ReservationDao reservationDao;

    ButtonPanel(JFrame frame, ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        panel = new JPanel();
        panel.setLayout(new GridLayout(NUM_OF_BUTTONS, 1, 5, 5));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        addButtons(frame);
    }

    JPanel getPanel() {
        return panel;
    }

    private void addButtons(JFrame frame) {
        panel.add(new Button(I18N.getString("newReservationButton"), e -> new NewReservation(frame, reservationDao)));
        panel.add(new Button(I18N.getString("cancelReservationButton"), e -> new CancelReservation(frame, reservationDao)));
        panel.add(new Button(I18N.getString("checkInButton"), e -> new CheckIn(frame, reservationDao)));
        panel.add(new Button(I18N.getString("checkOutButton"), e -> new CheckOut(frame, reservationDao)));
        panel.add(new Button(I18N.getString("reservationInfoButton"), e -> new ReservationInfo(frame, reservationDao)));
        panel.add(new Button(I18N.getString("roomInfoButton"), e -> new RoomInfo(frame)));
        panel.add(new Button(I18N.getString("settingsButton"), e -> new Settings(frame)));
    }
}
