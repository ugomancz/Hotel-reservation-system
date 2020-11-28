package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class NewReservation extends JDialog {

    Button cancelled, okay;
    JTextField name, phone, email, people;
    JComboBox<Integer> rooms;
    DatePicker fromDate, toDate;
    Reservation reservation;
    Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    GridBagConstraints gbc = new GridBagConstraints();

    public NewReservation() {
        super(MainWindow.frame, "New reservation", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);

        setEnabled(true);
        setLayout(new GridBagLayout());

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        fillOutFrame();
        pack();
        setVisible(true);
    }

        /**
         *
         * @param x coordination for gbc
         * @param y coordination for gbc
         * @param component to be placed onto frame
         */
        private void placeComponent(int x, int y, Component component) {
            gbc.gridx = x;
            gbc.gridy = y;
            add(component, gbc);
        }

    /**
     * Sets layout in frame using GridBagLayout
     */
    private void fillOutFrame() {
        gbc.anchor = GridBagConstraints.CENTER;

        placeComponent(0, 0, new JLabel("Name and surname: "));

        placeComponent(0,10, new JLabel("Phone: "));

        placeComponent(0,20, new JLabel("Email: "));

        placeComponent(0, 30, new JLabel("Number of people: "));

        placeComponent(0, 40, new JLabel("From: "));

        placeComponent(0, 50, new JLabel("To: "));

        placeComponent(0, 60, new JLabel("Room number: "));

        gbc.anchor = GridBagConstraints.LINE_START;

        name = new JTextField(16);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5,0, name);

        phone = new JTextField(16);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 10, phone);

        email = new JTextField(16);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 20, email);

        people = new JTextField(16);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(5, 30, people);

        fromDate = new DatePicker();
        fromDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        fromDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        fromDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        placeComponent(5, 40, fromDate);

        toDate = new DatePicker();
        toDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        toDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        toDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        placeComponent(5, 50, toDate);

        rooms = new JComboBox<>(array);
        rooms.setSelectedIndex(0);
        rooms.addActionListener(this::actionPerformed);
        rooms.setFont(new Font("Tahoma", Font.BOLD, 14));
        placeComponent(5,60, rooms);

        gbc.anchor = GridBagConstraints.SOUTH;

        okay = new Button("OK");
        okay.addActionListener(this::actionPerformed);
        placeComponent(0, 70, okay);

        cancelled = new Button("Cancel");
        cancelled.addActionListener(this::actionPerformed);
        placeComponent(5, 70, cancelled);

    }

    public Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelled)) {
            MainWindow.frame.setEnabled(true);
            dispose();
        } else if (e.getSource().equals(okay)) {
            String usedName = name.getText();
            String usedPhone = phone.getText();
            String usedMail = email.getText();
            String room = Objects.requireNonNull(rooms.getSelectedItem()).toString();
            LocalDate from = fromDate.getDate();
            LocalDate to = toDate.getDate();
            if (usedName.length() == 0) {
                JOptionPane.showInternalMessageDialog(null, "Name cannot be empty");
            } else if (usedPhone.length() == 0) {
                JOptionPane.showInternalMessageDialog(null, "Phone cannot be empty");
            } else if (tryParse(people.getText()) == null) {
                JOptionPane.showInternalMessageDialog(null, "Number of people is not number");
            } else if (fromDate.getDate().isBefore(LocalDate.now())){
                JOptionPane.showInternalMessageDialog(null, "Arrival date is before today");
            } else if(fromDate.getDate().isAfter(toDate.getDate())){
                JOptionPane.showInternalMessageDialog(null, "Departure need to be later than arrival");
            } else {
                int usedPeople = parseInt(people.getText());
                if (MainWindow.timetable.isFree(parseInt(room), from, to)) {
                    reservation = new Reservation(usedName, usedPhone, usedMail, usedPeople, parseInt(room), from, to,
                            ReservationStatus.PLANNED.toString());
                    MainWindow.frame.setEnabled(true);
                    dispose();
                } else {
                    JOptionPane.showInternalMessageDialog(null, "Room full");
                }
            }
        }
    }
}
