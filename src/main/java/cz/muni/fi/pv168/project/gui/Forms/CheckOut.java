package cz.muni.fi.pv168.project.gui.Forms;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.gui.Button;
import cz.muni.fi.pv168.project.reservations.Reservation;
import cz.muni.fi.pv168.project.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class CheckOut extends Form implements ActionListener {
    Dimension dimension = new Dimension(300, 300);
    JButton closeButton;
    JLabel label;
    JComboBox<String> pickReservation;
    ArrayList<Reservation> activeReservations = new ArrayList<>(Main.reservations.stream()
            .filter((x) -> x.getStatus() == ReservationStatus.planned)
            .collect(Collectors.toList()));
    String receipt = "<html>Client: %s<br/><br/>" +
            "Nights spent: %d<br/>" +
            "Room cost per night: %d<br/>" +
            "Local fees per person per night: %d<br/><br/>" +
            "<u>Total: %d</u></html>";

    public CheckOut() {
        super("Check-out");
        this.setSize(dimension);
        this.setResizable(false);
        this.setLayout(new BorderLayout(0, 0));

        pickReservation = new JComboBox<String>(activeReservations.stream()
                .map(Reservation::getName)
                .toArray(String[]::new));
        pickReservation.addActionListener(this);

        label = new JLabel("", SwingConstants.CENTER);

        closeButton = new Button("Close");
        closeButton.setMaximumSize(new Dimension(100, 30));
        closeButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        JPanel topPanel = new JPanel();
        topPanel.add(pickReservation, BorderLayout.CENTER);

        this.add(label, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void displayDetails(String name) {
        Reservation reservation = (Reservation) activeReservations.stream()
                .filter((x) -> x.getName().equals(name)).toArray()[0];
        label.setText(String.format(receipt, reservation.getName(), reservation.getLength(), 1200, 50, 5200));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            onClose();
        } else if (e.getSource().equals(pickReservation)) {
            displayDetails(pickReservation.getSelectedItem().toString());
        }
    }
}