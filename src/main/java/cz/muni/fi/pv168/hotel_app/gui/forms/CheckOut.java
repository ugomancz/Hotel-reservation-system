package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainPanel;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CheckOut extends JDialog {
    private JButton outButton, cancelButton;
    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private JComboBox<String> pickReservation;
    private final ArrayList<Reservation> activeReservations = new ArrayList<>(Main.reservations.stream()
            .filter((x) -> x.getStatus() == ReservationStatus.ONGOING)
            .collect(Collectors.toList()));
    private Reservation reservation;
    private final String receipt = "<html>Client: %s<br/><br/>" +
            "Nights spent: %d<br/>" +
            "Room cost per night: %d<br/>" +
            "Local fees per person per night: %d<br/><br/>" +
            "<u>Total: %d</u></html>";

    public CheckOut() {
        super(Main.frame, "Check-out", ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(250, 250));
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(Main.frame);

        add(label, BorderLayout.CENTER);
        add(addButtons(), BorderLayout.SOUTH);
        add(addComboBox(), BorderLayout.NORTH);
        setVisible(true);
    }

    private JPanel addButtons() {
        outButton = new Button("Check-out");
        outButton.addActionListener(this::actionPerformed);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);

        JPanel panel = new JPanel();
        panel.add(outButton);
        panel.add(cancelButton);
        return panel;
    }

    private JComboBox<String> addComboBox() {
        pickReservation = new JComboBox<>(activeReservations.stream()
                .map(Reservation::getName)
                .toArray(String[]::new));
        pickReservation.addActionListener(this::actionPerformed);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            reservation.setStatus(ReservationStatus.PAST);
            MainPanel.timetable.drawWeek(LocalDate.now());
            dispose();
        } else if (e.getSource().equals(pickReservation)) {
            displayDetails((String) pickReservation.getSelectedItem());
        } else if (e.getSource().equals(cancelButton)) {
            dispose();
        }
    }
}