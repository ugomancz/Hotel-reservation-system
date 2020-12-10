package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.DesignedDatePicker;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class NewReservation extends JDialog {

    private final ReservationDao reservationDao;
    Button cancelButton, okayButton;
    JTextField name, phone, email, people;
    JComboBox<Integer> rooms;
    DesignedDatePicker fromDate, toDate;
    Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    GridBagConstraints gbc = new GridBagConstraints();

    public NewReservation(JFrame frame, ReservationDao reservationDao) {
        super(frame, "New reservation", Dialog.ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        setSize(400, 400);

        setEnabled(true);
        setLayout(new GridBagLayout());

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        fillOutFrame();
        setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void addFields() {
        name = new JTextField(16);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 0, name);

        phone = new JTextField(16);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 10, phone);

        email = new JTextField(16);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 20, email);

        people = new JTextField(16);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 30, people);
    }

    private void addDatePickers() {
        fromDate = new DesignedDatePicker();
        placeComponent(5, 40, fromDate.getDatePicker());

        toDate = new DesignedDatePicker();
        placeComponent(5, 50, toDate.getDatePicker());
    }

    private void addComboBox() {
        rooms = new JComboBox<>(array);
        rooms.setSelectedIndex(0);
        rooms.addActionListener(this::actionPerformed);
        rooms.setFont(new Font("Tahoma", Font.BOLD, 14));
        placeComponent(5, 60, rooms);
    }

    private void addButtons() {
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 70, okayButton);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(5, 70, cancelButton);
    }

    private void fillOutFrame() {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(0, 0, new JLabel("Name and surname: "));
        placeComponent(0, 10, new JLabel("Phone: "));
        placeComponent(0, 20, new JLabel("Email: "));
        placeComponent(0, 30, new JLabel("Number of people: "));
        placeComponent(0, 40, new JLabel("From: "));
        placeComponent(0, 50, new JLabel("To: "));
        placeComponent(0, 60, new JLabel("Room number: "));

        gbc.anchor = GridBagConstraints.LINE_START;
        addFields();
        addDatePickers();
        addComboBox();

        gbc.anchor = GridBagConstraints.SOUTH;
        addButtons();
    }

    public Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(okayButton)) {
            String usedName = name.getText();
            String usedPhone = phone.getText();
            String usedMail = email.getText();
            String room = Objects.requireNonNull(rooms.getSelectedItem()).toString();
            LocalDate from = fromDate.getDate();
            LocalDate to = toDate.getDate();
            if (usedName.length() == 0) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty");
            } else if (usedPhone.length() == 0) {
                JOptionPane.showMessageDialog(this, "Phone cannot be empty");
            } else if (tryParse(people.getText()) == null) {
                JOptionPane.showMessageDialog(this, "Number of people is not number");
            } else if (fromDate.getDate().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Arrival date is before today");
            } else if (!toDate.getDate().isAfter(fromDate.getDate())) {
                JOptionPane.showMessageDialog(this, "Departure needs to be later than arrival");
            } else if (parseInt(people.getText()) > RoomDao.numberOfBeds(parseInt(room))) {
                JOptionPane.showMessageDialog(this, "Not enough beds in chosen room");
            } else {
                int usedPeople = parseInt(people.getText());
                if (reservationDao.isFree(parseInt(room), from, to)) {
                    reservationDao.create(new Reservation(usedName, usedPhone, usedMail, usedPeople, parseInt(room), from, to,
                            ReservationStatus.PLANNED.toString()));
                    Timetable.drawWeek(LocalDate.now());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Room full");
                }
            }
        }
    }
}
