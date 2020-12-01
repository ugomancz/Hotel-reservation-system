package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.gui.Timetable;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

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
    private JTextField idTextField;



    public CheckIn(ReservationDao reservationDao) {
        super(MainWindow.frame, "Check-in", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setSize(500, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setEnabled(true);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        initMap();
        initLayout();
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

    private void initComboBox( ) {
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setSelectedIndex(0);
        reservationPicker.setPreferredSize(new Dimension(300, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        //reservationPicker.setFont(cz.muni.fi.pv168.hotel_app.gui.Button.font);
        gbc.anchor = GridBagConstraints.CENTER;
    }

    /**
     * Sets layout in frame using GridBagLayout
     */
    private void initLayout() {
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 30, 0, 0);

        reservationPicker = new JComboBox<>();
        initComboBox();
        placeComponent(0, 0, reservationPicker);

        idTextField = new JTextField(16);
        placeComponent(0, 10, idTextField);

        String selected = (String) reservationPicker.getSelectedItem();
        Reservation res = reservationMap.get(selected);


        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("ID number: ");
        placeComponent(0, 10, idLabel);

        nameLabel = new JLabel();
        placeComponent(0, 20, nameLabel);

        phoneLabel = new JLabel();
        placeComponent(0, 30, phoneLabel);

        emailLabel = new JLabel();
        placeComponent(0, 40, emailLabel);

        guestLabel = new JLabel();
        placeComponent(0, 50, guestLabel);

        lengthLabel = new JLabel();
        placeComponent(0, 60, lengthLabel);

        roomLabel = new JLabel();
        placeComponent(0, 70, roomLabel);
        fillReservation(res);


        confirm = new Button("Confirm");
        placeComponent(0, 80, confirm);
        confirm.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 0, 0, 30);
        gbc.anchor = GridBagConstraints.EAST;
        cancel = new Button("Cancel");
        placeComponent(5, 80, cancel);
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
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            res.setStatus(ReservationStatus.ONGOING);
            if (idTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Guest ID can't be empty");
            } else {
                res.setGuestID(idTextField.getText());
                reservationDao.update(res);
                Timetable.drawWeek(LocalDate.now());
                dispose();
            }
        }
        if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            fillReservation(res);
        }
    }
}