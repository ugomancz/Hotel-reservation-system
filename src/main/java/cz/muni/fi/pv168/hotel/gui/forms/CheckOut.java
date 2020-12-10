package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.data.ReservationDao;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class CheckOut extends JDialog {

    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    GridBagConstraints gbc = new GridBagConstraints();
    private JButton outButton, cancelButton;
    private JComboBox<String> reservationPicker;

    public CheckOut(JFrame frame, ReservationDao reservationDao) {
        super(frame, "Check-out", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLayout(new GridBagLayout());
        setLocationRelativeTo(frame);
        setMinimumSize(new Dimension(250, 250));

        initMap();
        initLayout();
        setVisible(true);
    }

    private void initLayout() {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        addComponent(addComboBox(), 0);
        label.setPreferredSize(new Dimension(215, 120));
        addComponent(label, 1);
        addButtons();
        String selected = (String) reservationPicker.getSelectedItem();
        Reservation reservation = reservationMap.get(selected);
        if (reservation != null) {
            displayInfo(reservation);
        }
    }

    private void addButtons() {
        outButton = new Button("Check-out");
        outButton.addActionListener(this::actionPerformed);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_START;
        addComponent(outButton, 2);
        gbc.anchor = GridBagConstraints.LINE_END;
        addComponent(cancelButton, 2);
    }

    private void initMap() {
        for (Reservation reservation : reservationDao.findAll().stream()
                .filter((x) -> x.getStatus() == ReservationStatus.ONGOING)
                .collect(Collectors.toList())) {
            reservationMap.put(reservation.toString(), reservation);
        }
    }

    private void addComponent(JComponent component, int y) {
        gbc.gridy = y;
        add(component, gbc);
    }

    private JComboBox<String> addComboBox() {
        reservationPicker = new JComboBox<>();
        for (String reservation : reservationMap.keySet()) {
            reservationPicker.addItem(reservation);
        }
        reservationPicker.setPreferredSize(new Dimension(220, 22));
        reservationPicker.addActionListener(this::actionPerformed);
        return reservationPicker;
    }

    private int calculateTotalPrice(Reservation reservation) {
        return reservation.getLength() * RoomDao.getPricePerNight(reservation.getRoomNumber()) +
                reservation.getLength() * Constants.LOCAL_FEE * reservation.getHosts();
    }

    private void displayInfo(Reservation reservation) {
        String receipt = "<html>Client: %s<br/><br/>" +
                "Nights spent: %d<br/>" +
                "Room cost per night: %d<br/>" +
                "Local fees per person per night: %d<br/><br/>" +
                "<u>Total: %d</u></html>";
        label.setText(String.format(receipt, reservation.getName(), reservation.getLength(),
                RoomDao.getPricePerNight(reservation.getRoomNumber()),
                Constants.LOCAL_FEE, calculateTotalPrice(reservation)));
        pack();
    }

    private void closeReservation(Reservation reservation) {
        if (reservation == null) {
            JOptionPane.showMessageDialog(this, "A reservation has to be selected");
        } else {
            reservation.setDeparture(LocalDate.now());
            reservation.setStatus(ReservationStatus.PAST);
            reservationDao.update(reservation);
            Timetable.drawWeek(LocalDate.now());
            dispose();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            String selected = (String) reservationPicker.getSelectedItem();
            closeReservation(reservationMap.get(selected));
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(reservationMap.get(selected));
        } else if (e.getSource().equals(cancelButton)) {
            dispose();
        }
    }
}