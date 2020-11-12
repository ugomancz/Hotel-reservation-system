package cz.muni.fi.pv168.project.gui.Forms;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.gui.Button;
import cz.muni.fi.pv168.project.reservations.Reservation;
import cz.muni.fi.pv168.project.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckOut extends Form implements ActionListener {
    Dimension dimension = new Dimension(300, 300);
    JButton closeButton;
    JComboBox pickReservation;
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

        pickReservation = new JComboBox<>(Main.reservations.stream()
                .filter((x) -> x.getStatus() == ReservationStatus.ongoing)
                .map(Reservation::getName).toArray());

        JLabel label = new JLabel(String.format(receipt, "Customer", 3, 1200, 50, 5200), SwingConstants.CENTER);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            onClose();
        }
    }
}