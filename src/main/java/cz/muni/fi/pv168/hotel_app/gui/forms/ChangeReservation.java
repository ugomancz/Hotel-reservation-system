package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
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

    Button cancelButton, okayButton;
    JTextField name, phone, email, people;
    JComboBox<Integer> roomPicker;
    JComboBox<String> reservationPicker;
    DatePicker arrival, departure;

    Integer[] roomNumbers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    Map<String, Reservation> map = new HashMap<>();
    GridBagConstraints gbc = new GridBagConstraints();
    Dimension textFieldDimension = new Dimension(Integer.MAX_VALUE, 25);

    public ChangeReservation() {
        super(MainWindow.frame, "Change Reservation", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(MainWindow.frame);
        setLayout(new GridBagLayout());
        initMap();
        initLayout();
        setSize(400, 400);
        setResizable(false);
        setVisible(true);
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel("Reservation: "), gbc, 0, 0);
        addComponent(new JLabel("Name and surname: "), gbc, 0, 10);
        addComponent(new JLabel("Phone: "), gbc, 0, 20);
        addComponent(new JLabel("Email: "), gbc, 0, 30);
        addComponent(new JLabel("Number of people: "), gbc, 0, 40);
        addComponent(new JLabel("From: "), gbc, 0, 50);
        addComponent(new JLabel("To: "), gbc, 0, 60);
        addComponent(new JLabel("Room number: "), gbc, 0, 70);
        addButtons();

        gbc.anchor = GridBagConstraints.LINE_START;
        addTextFields();
        addDatePickers();
        addComboBoxes();
    }

    private void addComponent(JComponent component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void addTextFields() {
        name = new JTextField(13);
        name.setMaximumSize(textFieldDimension);
        name.setEditable(true);
        addComponent(name, gbc, 5, 10);

        phone = new JTextField(13);
        phone.setMaximumSize(textFieldDimension);
        phone.setEditable(true);
        addComponent(phone, gbc, 5, 20);

        email = new JTextField(13);
        email.setMaximumSize(textFieldDimension);
        email.setEditable(true);
        addComponent(email, gbc, 5, 30);

        people = new JTextField(13);
        people.setMaximumSize(textFieldDimension);
        people.setEditable(true);
        addComponent(people, gbc, 5, 40);
    }

    private void addDatePickers() {
        arrival = new DatePicker();
        arrival.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        arrival.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        arrival.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(arrival, gbc, 5, 50);

        departure = new DatePicker();
        departure.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        departure.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        departure.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(departure, gbc, 5, 60);
    }

    private void addButtons() {
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        addComponent(okayButton, gbc, 0, 80);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        addComponent(cancelButton, gbc, 5, 80);
    }

    private void addComboBoxes() {
        reservationPicker = new JComboBox<>();
        for (String name : map.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setSelectedIndex(0);
        reservationPicker.addActionListener(this::actionPerformed);
        reservationPicker.setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(reservationPicker, gbc, 5, 0);

        roomPicker = new JComboBox<>(roomNumbers);
        roomPicker.setSelectedIndex(0);
        roomPicker.addActionListener(this::actionPerformed);
        roomPicker.setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(roomPicker, gbc, 5, 70);
    }

    private void initMap() {
        map = new HashMap<>();
        for (Reservation reservation : Main.reservations) {
            map.put(reservation.getName(), reservation);
        }
    }

    private void displayInfo(Reservation reservation) {
        name.setText(reservation.getName());
        phone.setText(reservation.getPhone());
        email.setText(reservation.getEmail());
        people.setText(Integer.toString(reservation.getHosts()));
        arrival.setDate(reservation.getArrival());
        departure.setDate(reservation.getDeparture());
        roomPicker.setSelectedIndex(reservation.getRoomNumber() - 1);
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(okayButton)) {
            // update reservation
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            displayInfo(map.get(selected));
        }
    }
}
