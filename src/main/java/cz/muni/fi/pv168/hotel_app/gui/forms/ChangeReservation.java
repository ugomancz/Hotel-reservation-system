package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.gui.Timetable;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Kostik
 */
public class ChangeReservation extends JDialog {

    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private Button cancelButton, okayButton;
    private JTextField nameField, phoneField, emailField, guestsField;
    private JComboBox<Integer> roomPicker;
    private JComboBox<String> reservationPicker;
    private DatePicker arrival, departure;

    public ChangeReservation(ReservationDao reservationDao) {
        super(MainWindow.frame, "Change Reservation", ModalityType.APPLICATION_MODAL);
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
        for (Reservation reservation : reservationDao.findAll()) {
            reservationMap.put(reservation.toString(), reservation);
        }
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel("Reservation: "), 0, 0);
        addComponent(new JLabel("Name and surname: "), 0, 10);
        addComponent(new JLabel("Phone: "), 0, 20);
        addComponent(new JLabel("Email: "), 0, 30);
        addComponent(new JLabel("Guests: "), 0, 40);
        addComponent(new JLabel("From: "), 0, 50);
        addComponent(new JLabel("To: "), 0, 60);
        addComponent(new JLabel("Room number: "), 0, 70);
        addButtons();

        gbc.anchor = GridBagConstraints.LINE_START;
        nameField = (JTextField) addComponent(new JTextField(20), 5, 10);
        phoneField = (JTextField) addComponent(new JTextField(20), 5, 20);
        emailField = (JTextField) addComponent(new JTextField(20), 5, 30);
        guestsField = (JTextField) addComponent(new JTextField(2), 5, 40);
        addDatePickers();
        addComboBoxes();
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
        addComponent(arrival, 5, 50);

        departure = new DatePicker();
        departure.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        departure.getComponentToggleCalendarButton().setBackground(Button.background);
        departure.getComponentToggleCalendarButton().setFont(Button.font);
        addComponent(departure, 5, 60);
    }

    private void addButtons() {
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        addComponent(okayButton, 5, 80);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        addComponent(cancelButton, 0, 80);
    }

    private void addComboBoxes() {
        reservationPicker = new JComboBox<>();
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setSelectedIndex(0);
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        addComponent(reservationPicker, 5, 0);

        roomPicker = new JComboBox<>();
        for (int i = 0; i < Constants.NUMBER_OF_ROOMS; i++) {
            roomPicker.addItem(i + 1);
        }
        roomPicker.setSelectedIndex(0);
        roomPicker.addActionListener(this::actionPerformed);
        addComponent(roomPicker, 5, 70);
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
        try {
            reservation.setHosts(Integer.parseInt(guestsField.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Field \"Guests\" has to be a number");
            return false;
        }
        reservation.setName(nameField.getText());
        reservation.setPhone(phoneField.getText());
        reservation.setEmail(emailField.getText());
        reservation.setArrival(arrival.getDate());
        reservation.setDeparture(departure.getDate());
        reservation.setRoomNumber(roomPicker.getSelectedIndex() + 1);
        Timetable.drawWeek(arrival.getDate());
        return true;
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(okayButton)) {
            String selected = (String) reservationPicker.getSelectedItem();
            if (updateReservation(reservationMap.get(selected))) {
                dispose();
            }
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(reservationMap.get(selected));
        }
    }
}
