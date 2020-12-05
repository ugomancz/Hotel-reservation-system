package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.gui.Timetable;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel_app.rooms.RoomDao;

import javax.swing.*;
import java.awt.*;
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
    private Button cancelButton, confirmButton, editButton;
    private JTextField nameField, phoneField, emailField, guestsField;
    private JComboBox<Integer> roomPicker;
    private JComboBox<String> reservationPicker;
    private DatePicker arrival, departure;

    public ReservationInfo(ReservationDao reservationDao) {
        super(MainWindow.frame, "Reservation info", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLocationRelativeTo(MainWindow.frame);
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
        textFieldsEditable(false);
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
        arrival.getComponentDateTextField().setDisabledTextColor(Color.black);
        addComponent(arrival, 1, 5);

        departure = new DatePicker();
        departure.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        departure.getComponentToggleCalendarButton().setBackground(Button.background);
        departure.getComponentToggleCalendarButton().setFont(Button.font);
        departure.getComponentDateTextField().setDisabledTextColor(Color.black);
        addComponent(departure, 1, 6);
    }

    private void addButtons() {
        confirmButton = new Button("Confirm");
        confirmButton.addActionListener(this::actionPerformed);
        addComponent(confirmButton, 0, 8);

        gbc.anchor = GridBagConstraints.LINE_START;
        editButton = new Button("Edit");
        editButton.addActionListener(this::actionPerformed);
        addComponent(editButton, 1, 8);

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
        roomPicker.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setForeground(Color.BLACK);
                super.paint(g);
            }
        });
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
            showError("Not enough beds in chosen room");
            return false;
        }
        if (!reservationDao.isFree(room, arrival.getDate(), departure.getDate(), reservation.getId())) {
            showError("Selected room isn't free at that time");
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
        Timetable.drawWeek(arrival.getDate());
        return true;
    }

    private void textFieldsEditable(boolean value) {
        nameField.setEditable(value);
        phoneField.setEditable(value);
        emailField.setEditable(value);
        arrival.setEnabled(value);
        departure.setEnabled(value);
        roomPicker.setEnabled(value);
        guestsField.setEditable(value);
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
        } else if (e.getSource().equals(editButton)) {
            if (reservationPicker.getSelectedItem() == null) {
                showError("A reservation has to be selected");
            } else {
                textFieldsEditable(true);
            }
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(reservationMap.get(selected));
        }
    }
}
