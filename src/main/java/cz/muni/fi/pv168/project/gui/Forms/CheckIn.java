package cz.muni.fi.pv168.project.gui.Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;


import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.gui.MainPanel;
import cz.muni.fi.pv168.project.reservations.Reservation;
import cz.muni.fi.pv168.project.reservations.ReservationStatus;

public class CheckIn extends Form implements ActionListener {

    GridBagConstraints gbc = new GridBagConstraints();
    JTextField nameField, phoneField, IDfield, lengthField, guestField, emailField;
    JComboBox<Integer> roomBox;
    Button confirm, cancel, change, findReservation;
    Reservation reservation;


    public CheckIn() {
        super("Check-in");
        this.setSize(new Dimension(500, 400));

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        gbc.weightx = 0;
        gbc.weighty = 0.1;






        JLabel IDnumber = new JLabel("ID number ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(IDnumber, gbc);
        IDfield = new JTextField(16);
        IDfield.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(IDfield);
        findReservation = new Button("Find reservation");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(findReservation);


        JLabel nameLabel = new JLabel("Name ");
        gbc.gridx =0;
        gbc.gridy =1;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(nameLabel, gbc);
        nameField = new JTextField(16);
        nameField.setEditable(true);
        gbc.gridx =1;
        gbc.gridy =1;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(nameField, gbc);


        JLabel phoneLabel = new JLabel("Phone number ");
        gbc.gridx =0;
        gbc.gridy =2;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(phoneLabel, gbc);
        phoneField = new JTextField(16);
        phoneField.setEditable(false);
        gbc.gridx =1;
        gbc.gridy =2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(phoneField, gbc);

        JLabel emailLabel = new JLabel("E-mail ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(emailLabel, gbc);
        emailField = new JTextField(16);
        emailField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(emailField, gbc);


        JLabel roomLabel = new JLabel("Room number ");
        gbc.gridx =0;
        gbc.gridy =4;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(roomLabel, gbc);

        Integer[] rooms = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        roomBox = new JComboBox<>(rooms);
        //roomField = new JTextField(4);
        //roomField.setEditable(true);
        roomBox.setSelectedIndex(0);
        roomBox.addActionListener(this);
        gbc.gridx =1;
        gbc.gridy =4;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(roomBox, gbc);


        JLabel lengthLabel = new JLabel("Length of stay ");
        gbc.gridx =0;
        gbc.gridy =5;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(lengthLabel, gbc);
        lengthField = new JTextField(4);
        lengthField.setEditable(false);
        gbc.gridx =1;
        gbc.gridy =5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(lengthField, gbc);
        JLabel days = new JLabel(" days");
        gbc.gridx = 2;
        gbc.gridy = 5;
        this.add(days, gbc);


        JLabel guestLabel = new JLabel("Number of guests");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(guestLabel, gbc);
        guestField = new JTextField(4);
        guestField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(guestField, gbc);



        confirm = new Button("Confirm");
        cancel = new Button("Cancel");
        change = new Button("Change Reservation");

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        this.add(cancel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        this.add(confirm, gbc);

        gbc.gridx =2;
        gbc.gridy =1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(change, gbc);



        cancel.addActionListener(this);
        confirm.addActionListener(this);
        confirm.setEnabled(false);

        change.addActionListener(this);
        change.setEnabled(false);
        findReservation.addActionListener(this);

        //Reservation test = new Reservation("Kokot", "123456789", "kokot@email.cz", 2, 5, LocalDate.now(), LocalDate.now().plusDays(2));




    }

    private Reservation findReservation(String name) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getName().equals(name) && reservation.getArrival().equals(LocalDate.now())) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Cancel")) {
            onClose();

        } else if (action.equals("Confirm")) {

            reservation.setStatus(ReservationStatus.ongoing);
            MainPanel.timetable.drawWeek(LocalDate.now());
            onClose();


        } else if (action.equals("Change reservation")) {
            JOptionPane.showMessageDialog(this, "This function is not implemented yet");
        } else if (action.equals("Find reservation")) {
            reservation = findReservation(nameField.getText());
            //reservation was found
            if (reservation != null) {
                int length = Period.between(reservation.getArrival(), reservation.getDeparture()).getDays();

                nameField.setText(reservation.getName());
                roomBox.setSelectedIndex(reservation.getRoomNumber() - 1);
                phoneField.setText(reservation.getPhone());
                lengthField.setText(String.valueOf(length));
                emailField.setText(reservation.getEmail());
                guestField.setText(String.valueOf(reservation.getHosts()));
                confirm.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(this, "No reservation found");
            }

        }


    }
}
