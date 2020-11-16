package cz.muni.fi.pv168.project.gui.Forms;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.gui.Button;
import cz.muni.fi.pv168.project.gui.MainPanel;
import cz.muni.fi.pv168.project.reservations.Reservation;
import cz.muni.fi.pv168.project.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CheckOut extends Form implements ActionListener {
    private JButton outButton, cancelButton;
    private JLabel label = new JLabel("", SwingConstants.CENTER);
    private JComboBox<String> pickReservation;
    private ArrayList<Reservation> activeReservations = new ArrayList<>(Main.reservations.stream()
            .filter((x) -> x.getStatus() == ReservationStatus.ONGOING)
            .collect(Collectors.toList()));
    private Reservation reservation;
    private String receipt = "<html>Client: %s<br/><br/>" +
            "Nights spent: %d<br/>" +
            "Room cost per night: %d<br/>" +
            "Local fees per person per night: %d<br/><br/>" +
            "<u>Total: %d</u></html>";

    public CheckOut() {
        super("Check-out");
        this.setSize(new Dimension(250, 250));
        this.setResizable(false);
        this.setLayout(new BorderLayout(0, 0));

        this.add(label, BorderLayout.CENTER);
        this.add(addButtons(), BorderLayout.SOUTH);
        this.add(addComboBox(), BorderLayout.NORTH);
    }

    private JPanel addButtons() {
        outButton = new Button("Check-out");
        outButton.addActionListener(this);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(outButton);
        panel.add(cancelButton);
        return panel;
    }

    private JComboBox<String> addComboBox() {
        pickReservation = new JComboBox<>(activeReservations.stream()
                .map(Reservation::getName)
                .toArray(String[]::new));
        pickReservation.addActionListener(this);
        return pickReservation;
    }

    private void displayDetails(String name) {
        try {
            reservation = (Reservation) activeReservations.stream()
                    .filter((x) -> x.getName().equals(name)).toArray()[0];
        } catch (Exception e) {
            throw new RuntimeException("It seems this reservation doesn't exist", e);
        }
        label.setText(String.format(receipt, reservation.getName(), reservation.getLength(), 1200, 50, 5200));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            reservation.setStatus(ReservationStatus.PAST);
            MainPanel.timetable.drawWeek(LocalDate.now());
            onClose();
        } else if (e.getSource().equals(pickReservation)) {
            displayDetails((String) pickReservation.getSelectedItem());
        } else if (e.getSource().equals(cancelButton)) {
            onClose();
        }
    }
}