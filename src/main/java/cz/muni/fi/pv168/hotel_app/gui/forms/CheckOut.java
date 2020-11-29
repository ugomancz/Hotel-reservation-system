package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.gui.Timetable;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CheckOut extends JDialog {

    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private JButton outButton, cancelButton;
    private JComboBox<String> pickReservation;

    public CheckOut(ReservationDao reservationDao) {
        super(MainWindow.frame, "Check-out", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLayout(new BorderLayout(0, 0));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setMinimumSize(new Dimension(250, 250));

        initMap();
        add(label, BorderLayout.CENTER);
        add(addButtons(), BorderLayout.SOUTH);
        add(addComboBox(), BorderLayout.NORTH);

        pack();
        setVisible(true);
    }

    private void initMap() {
        for (Reservation reservation : reservationDao.findAll()) {
            if (reservation.getStatus() == ReservationStatus.ONGOING) {
                reservationMap.put(reservation.toString(), reservation);
            }
        }
    }

    private JPanel addButtons() {
        outButton = new Button("Check-out");
        outButton.addActionListener(this::actionPerformed);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);

        JPanel panel = new JPanel();
        panel.add(cancelButton);
        panel.add(outButton);
        return panel;
    }

    private JComboBox<String> addComboBox() {
        pickReservation = new JComboBox<>();
        for (String reservation : reservationMap.keySet()) {
            pickReservation.addItem(reservation);
        }
        pickReservation.addActionListener(this::actionPerformed);
        return pickReservation;
    }

    private void displayDetails(Reservation reservation) {
        String receipt = "<html>Client: %s<br/><br/>" +
                "Nights spent: %d<br/>" +
                "Room cost per night: %d<br/>" +
                "Local fees per person per night: %d<br/><br/>" +
                "<u>Total: %d</u></html>";
        label.setText(String.format(receipt, reservation.getName(), reservation.getLength(), 1200, 50, 5200));
        pack();
    }

    private void closeReservation(Reservation reservation) {
        if (reservation == null) {
            JOptionPane.showMessageDialog(this,
                    "No reservation picked");
        } else {
            reservation.setDeparture(LocalDate.now());
            reservation.setStatus(ReservationStatus.PAST);
            Timetable.drawWeek(LocalDate.now());
            dispose();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            String selected = (String) pickReservation.getSelectedItem();
            closeReservation(reservationMap.get(selected));
        } else if (e.getSource().equals(pickReservation)) {
            String selected = (String) pickReservation.getSelectedItem();
            displayDetails(reservationMap.get(selected));
        } else if (e.getSource().equals(cancelButton)) {
            dispose();
        }
    }
}