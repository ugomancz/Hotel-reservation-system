package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.Period;

public class CheckIn extends JDialog {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private JTextField nameField, phoneField, lengthField, guestField, emailField;
    private JComboBox<Integer> roomBox;
    private Button confirm;
    private Reservation reservation;


    public CheckIn() {
        super(MainWindow.frame, "Check-in", ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(500, 400));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setEnabled(true);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        fillOutFrame();
        setVisible(true);

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
        gbc.weightx = 0;
        gbc.weighty = 0.1;

        gbc.anchor = GridBagConstraints.WEST;
        JLabel IDnumber = new JLabel("ID number ");
        placeComponent(0, 0, IDnumber);

        JLabel nameLabel = new JLabel("Name ");
        placeComponent(0, 1, nameLabel);

        JLabel phoneLabel = new JLabel("Phone number ");
        placeComponent(0, 2, phoneLabel);

        JLabel emailLabel = new JLabel("E-mail ");
        placeComponent(0, 3, emailLabel);

        JLabel roomLabel = new JLabel("Room number ");
        placeComponent(0, 4, roomLabel);

        JLabel lengthLabel = new JLabel("Length of stay ");
        placeComponent(0, 5, lengthLabel);

        JLabel guestLabel = new JLabel("Number of guests");
        placeComponent(0, 6, guestLabel);

        gbc.anchor = GridBagConstraints.CENTER;
        JTextField IDfield = new JTextField(16);
        IDfield.setEditable(false);
        placeComponent(1, 0, IDfield);

        nameField = new JTextField(16);
        nameField.setEditable(true);
        placeComponent(1, 1, nameField);

        phoneField = new JTextField(16);
        phoneField.setEditable(false);
        placeComponent(1, 2, phoneField);

        emailField = new JTextField(16);
        emailField.setEditable(false);
        placeComponent(1,3, emailField);

        Integer[] rooms = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        roomBox = new JComboBox<>(rooms);
        roomBox.setSelectedIndex(0);
        roomBox.addActionListener(this::actionPerformed);
        placeComponent(1, 4, roomBox);

        lengthField = new JTextField(4);
        lengthField.setEditable(false);
        placeComponent(1, 5, lengthField);

        guestField = new JTextField(4);
        guestField.setEditable(false);
        placeComponent(1, 6, guestField);

        gbc.anchor = GridBagConstraints.EAST;
        Button findReservation = new Button("Find reservation");
        placeComponent(2, 0, findReservation);
        findReservation.addActionListener(this::actionPerformed);

        Button change = new Button("Change Reservation");
        placeComponent(2, 1, change);
        change.addActionListener(this::actionPerformed);
        change.setEnabled(false);

        JLabel nights = new JLabel(" nights");
        placeComponent(2, 5, nights);

        gbc.anchor = GridBagConstraints.SOUTH;
        confirm = new Button("Confirm");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        placeComponent(0, 7, confirm);
        confirm.addActionListener(this::actionPerformed);
        confirm.setEnabled(false);

        Button cancel = new Button("Cancel");
        placeComponent(2, 7, cancel);
        cancel.addActionListener(this::actionPerformed);
    }

    /**
     * In case of a found existing reservation, fills out all the information
     */
    private void fillReservation() {
        int length = Period.between(reservation.getArrival(), reservation.getDeparture()).getDays();

        nameField.setText(reservation.getName());
        roomBox.setSelectedIndex(reservation.getRoomNumber() - 1);
        phoneField.setText(reservation.getPhone());
        lengthField.setText(String.valueOf(length));
        emailField.setText(reservation.getEmail());
        guestField.setText(String.valueOf(reservation.getHosts()));
        confirm.setEnabled(true);
    }

    /**
     * searches for an existing reservation using name and Today date as keys
     * @param name : key for searching for an existing reservation
     * @return existing reservation
     */
    private Reservation findReservation(String name) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getName().equals(name) && reservation.getArrival().equals(LocalDate.now())) {
                return reservation;
            }
        }
        return null;
    }

    private void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        //maybe switch to e.getSource().equals(cancel)
        if (action.equals("Cancel")) {
            dispose();
        }
        if (action.equals("Confirm")) {
            //if a reservation is confirmed it's status is changed
            reservation.setStatus(ReservationStatus.ONGOING);
            MainWindow.timetable.drawWeek(LocalDate.now());
            dispose();
        }
        if (action.equals("Change reservation")) {
            JOptionPane.showMessageDialog(this, "This function is not implemented yet");
        }
        if (action.equals("Find reservation")) {
            reservation = findReservation(nameField.getText());
            //reservation was found
            if (reservation != null) {
                fillReservation();
            } else {
                JOptionPane.showMessageDialog(this, "No check-in scheduled for today");
            }

        }
    }
}