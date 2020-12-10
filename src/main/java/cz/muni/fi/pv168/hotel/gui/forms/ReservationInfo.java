package cz.muni.fi.pv168.hotel.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel.data.ReservationDao;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class ReservationInfo extends JDialog {

    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private Button cancelButton, confirmButton;
    private JTextField nameField, phoneField, emailField, guestsField;
    private JComboBox<Integer> roomPicker;
    private JComboBox<String> reservationPicker;
    private DatePicker arrival, departure;

    public ReservationInfo(JFrame frame, ReservationDao reservationDao) {
        super(frame, "Reservation info", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLocationRelativeTo(frame);
        setLayout(new GridBagLayout());
        initMap();
        initLayout();
        setSize(400, 400);
        setResizable(false);
        setVisible(true);
    }

    private void initMap() {
        for (Reservation reservation : reservationDao.findAll().stream()
                .filter((x) -> x.getStatus() == ReservationStatus.PLANNED)
                .collect(Collectors.toList())) {
            reservationMap.put(reservation.toString(), reservation);
        }
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel("Reservation: "), 0, 0);
        addComponent(new JLabel("Name and surname: "), 0, 1);
        addComponent(new JLabel("Phone: "), 0, 2);
        addComponent(new JLabel("Email: "), 0, 3);
        addComponent(new JLabel("Guests: "), 0, 4);
        addComponent(new JLabel("From: "), 0, 5);
        addComponent(new JLabel("To: "), 0, 6);
        addComponent(new JLabel("Room number: "), 0, 7);
        addButtons();

        gbc.anchor = GridBagConstraints.LINE_START;
        nameField = (JTextField) addComponent(new JTextField(20), 1, 1);
        phoneField = (JTextField) addComponent(new JTextField(20), 1, 2);
        emailField = (JTextField) addComponent(new JTextField(20), 1, 3);
        guestsField = (JTextField) addComponent(new JTextField(2), 1, 4);
        addDatePickers();
        addComboBoxes();
        String selected = (String) reservationPicker.getSelectedItem();
        Reservation reservation = reservationMap.get(selected);
        if (reservation != null) {
            displayInfo(reservation);
        }
    }

    private JComponent addComponent(JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
        return component;
    }


    private void addDatePickers() {
        arrival = new DatePicker();
        arrival.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        arrival.getComponentToggleCalendarButton().setBackground(Button.background);
        arrival.getComponentToggleCalendarButton().setFont(Button.font);
        addComponent(arrival, 1, 5);

        departure = new DatePicker();
        departure.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        departure.getComponentToggleCalendarButton().setBackground(Button.background);
        departure.getComponentToggleCalendarButton().setFont(Button.font);
        addComponent(departure, 1, 6);
    }

    private void addButtons() {
        confirmButton = new Button("Confirm");
        confirmButton.addActionListener(this::actionPerformed);
        addComponent(confirmButton, 0, 8);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 10, 0, 10);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        addComponent(cancelButton, 1, 8);
    }

    private void addComboBoxes() {
        reservationPicker = new JComboBox<>();
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        addComponent(reservationPicker, 1, 0);

        roomPicker = new JComboBox<>();
        for (int i = 0; i < RoomDao.numberOfRooms(); i++) {
            roomPicker.addItem(i + 1);
        }
        roomPicker.addActionListener(this::actionPerformed);
        addComponent(roomPicker, 1, 7);
    }

    private void displayInfo(Reservation reservation) {
        nameField.setText(reservation.getName());
        phoneField.setText(reservation.getPhone());
        emailField.setText(reservation.getEmail());
        guestsField.setText(Integer.toString(reservation.getHosts()));
        arrival.setDate(reservation.getArrival());
        departure.setDate(reservation.getDeparture());
        roomPicker.setSelectedIndex(reservation.getRoomNumber() - 1);
    }

    private boolean updateReservation(Reservation reservation) {
        int guests;
        int room = roomPicker.getSelectedIndex() + 1;
        try {
            guests = Integer.parseInt(guestsField.getText());
        } catch (Exception e) {
            showError("Field \"Guests\" has to be a number");
            return false;
        }
        if (RoomDao.numberOfBeds(room) < guests) {
            showError("Not enough beds in the chosen room");
            return false;
        }
        if (!reservationDao.isFree(room, arrival.getDate(), departure.getDate(), reservation.getId())) {
            showError("Selected room isn't free at that time");
            return false;
        }
        if (!departure.getDate().isAfter(arrival.getDate())) {
            showError("Please check the selected dates");
            return false;
        }
        reservation.setHosts(guests);
        reservation.setName(nameField.getText());
        reservation.setPhone(phoneField.getText());
        reservation.setEmail(emailField.getText());
        reservation.setArrival(arrival.getDate());
        reservation.setDeparture(departure.getDate());
        reservation.setRoomNumber(room);
        reservationDao.update(reservation);
        Timetable.drawWeek(reservation.getArrival());
        return true;
    }

    private void showError(String error) {
        JOptionPane.showMessageDialog(this, error);
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(confirmButton)) {
            if (reservationPicker.getSelectedItem() == null) {
                showError("A reservation has to be selected");
            } else {
                String selected = (String) reservationPicker.getSelectedItem();
                if (updateReservation(reservationMap.get(selected))) {
                    dispose();
                }
            }
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(reservationMap.get(selected));
        }
    }
}
