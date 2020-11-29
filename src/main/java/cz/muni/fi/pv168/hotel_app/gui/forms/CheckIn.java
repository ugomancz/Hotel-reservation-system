package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.gui.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lukas Hasik
 */

public class CheckIn extends JDialog {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private JLabel nameLabel, phoneLabel, emailLabel, guestLabel, roomLabel, lengthLabel;
    private Button confirm, cancel;
    private final ReservationDao reservationDao;
    private JComboBox<String> reservationPicker;
    private final Map<String, Reservation> reservationMap = new HashMap<>();



    public CheckIn(ReservationDao reservationDao) {
        super(MainWindow.frame, "Check-in", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setEnabled(true);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        initMap();
        fillOutFrame();
        setVisible(true);

    }

    /**
     * fills map with reservations starting today
     */
    private void initMap() {
        for (Reservation reservation : reservationDao.findAll().stream()
                .filter(x -> x.getArrival().equals(LocalDate.now()))
                .collect(Collectors.toList())) {
            reservationMap.put(reservation.toString(), reservation);
        }
    }


    /**
     *
     * @param x coordination for gbc
     * @param y coordination for gbc
     * @param component to be placed onto frame
     */
    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    /**
     * Sets layout in frame using GridBagLayout
     */
    private void fillOutFrame() {
        reservationPicker = new JComboBox<>();
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setSelectedIndex(0);
        reservationPicker.setPreferredSize(new Dimension(300, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        reservationPicker.setFont(cz.muni.fi.pv168.hotel_app.gui.Button.font);
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(0, 1, reservationPicker);

        String selected = (String) reservationPicker.getSelectedItem();
        Reservation res = reservationMap.get(selected);

        gbc.anchor = GridBagConstraints.WEST;
        nameLabel = new JLabel();
        placeComponent(0, 2, nameLabel);

        phoneLabel = new JLabel();
        placeComponent(0, 3, phoneLabel);

        emailLabel = new JLabel();
        placeComponent(0, 4, emailLabel);

        guestLabel = new JLabel();
        placeComponent(0, 5, guestLabel);

        lengthLabel = new JLabel();
        placeComponent(0, 6, lengthLabel);

        roomLabel = new JLabel();
        placeComponent(0, 7, roomLabel);
        fillReservation(res);


        confirm = new Button("Confirm");
        placeComponent(0, 8, confirm);
        confirm.addActionListener(this::actionPerformed);

        cancel = new Button("Cancel");
        placeComponent(1, 8, cancel);
        cancel.addActionListener(this::actionPerformed);

    }

    /**
     * Fills labels with information obtained from selected reservation
     */
    private void fillReservation(Reservation res) {
        nameLabel.setText("Name and Surname: " + res.getName());
        phoneLabel.setText("Phone number: " + res.getPhone());
        emailLabel.setText("Email: " + res.getEmail());
        guestLabel.setText("Number of guests: " + res.getHosts());
        lengthLabel.setText("Length of stay: " + res.getLength() + " nights");
        roomLabel.setText("Room number: " + res.getRoomNumber());
    }

    private void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cancel)) {
            dispose();
        }
        if (e.getSource().equals(confirm)) {
            dispose();
        }
        if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            fillReservation(res);
        }
    }
}