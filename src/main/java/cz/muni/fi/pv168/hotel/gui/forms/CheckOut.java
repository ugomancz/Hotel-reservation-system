package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class CheckOut {

    private static final I18N I18N = new I18N(CheckOut.class);
    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JDialog dialog;
    private JButton outButton, cancelButton;
    private JComboBox<String> reservationPicker;

    public CheckOut(JFrame frame, ReservationDao reservationDao) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setMinimumSize(new Dimension(250, 250));
        dialog.getRootPane().registerKeyboardAction(this::actionPerformed, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        initMap();
        initLayout();
        dialog.setVisible(true);
    }

    private void initLayout() {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        addComponent(addComboBox(), 0);
        label.setPreferredSize(new Dimension(215, 130));
        addComponent(label, 1);
        addButtons();
        String selected = (String) reservationPicker.getSelectedItem();
        Reservation reservation = reservationMap.get(selected);
        if (reservation != null) {
            displayInfo(reservation);
        }
    }

    private void addButtons() {
        outButton = new Button(I18N.getString("confirmButton"));
        outButton.addActionListener(this::actionPerformed);
        cancelButton = new Button(I18N.getString("cancelButton"));
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
        dialog.add(component, gbc);
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
        /* calculates length of stay if departure != day of checkout */
        int length = reservation.getDeparture().isEqual(LocalDate.now()) ?
                reservation.getLength() : LocalDate.now().compareTo(reservation.getArrival());
        return length * RoomDao.getPricePerNight(reservation.getRoomNumber()) +
                length * Constants.LOCAL_FEE * reservation.getGuests();
    }

    private void displayInfo(Reservation reservation) {
        String receipt = "<html>" + I18N.getString("clientLabel") + ": %s<br/><br/>" +
                I18N.getString("nightsLabel") + ": %d<br/>" +
                I18N.getString("guestsLabel") + ": %d<br/>" +
                I18N.getString("roomCostLabel") + ": %d<br/>" +
                I18N.getString("feesLabel") + ": %d<br/><br/>" +
                "<u>" + I18N.getString("totalLabel") + ": %d</u></html>";
        label.setText(String.format(receipt, reservation.getName(),
                LocalDate.now().compareTo(reservation.getArrival()),
                reservation.getGuests(),
                RoomDao.getPricePerNight(reservation.getRoomNumber()),
                Constants.LOCAL_FEE, calculateTotalPrice(reservation)));
        dialog.pack();
    }

    private void closeReservation(Reservation reservation) {
        if (reservation == null) {
            JOptionPane.showMessageDialog(dialog, I18N.getString("selectionError"));
        } else {
            reservation.setDeparture(LocalDate.now());
            reservation.setStatus(ReservationStatus.PAST);
            reservationDao.update(reservation);
        }
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            String selected = (String) reservationPicker.getSelectedItem();
            closeReservation(reservationMap.get(selected));
            Timetable.drawWeek(LocalDate.now());
            dialog.dispose();
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(reservationMap.get(selected));
        } else if (e.getSource().equals(cancelButton) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        }
    }
}