package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class NewReservation {

    Button cancelled, okay;
    JTextField name, phone, email, people;
    JComboBox<Integer> rooms;
    DatePicker fromDate, toDate;
    Reservation reservation;
    JFrame frame = new JFrame();
    Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    GridBagConstraints gbc = new GridBagConstraints();

    public NewReservation() {
        frame.setLocationRelativeTo(MainWindow.frame);
        frame.setVisible(true);

        MainWindow.frame.setEnabled(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                MainWindow.frame.setEnabled(true);
                frame.dispose();
            }
        });
        frame.setSize(new Dimension(400, 400));
        frame.setLayout(new GridBagLayout());
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(new JLabel("Name and surname: "), gbc);

        gbc.gridy = 10;
        frame.add(new JLabel("Phone: "), gbc);

        gbc.gridy = 20;
        frame.add(new JLabel("Email: "), gbc);

        gbc.gridy = 30;
        frame.add(new JLabel("Number of people: "), gbc);

        gbc.gridy = 40;
        frame.add(new JLabel("From: "), gbc);

        gbc.gridy = 50;
        frame.add(new JLabel("To: "), gbc);

        gbc.gridy = 60;
        frame.add(new JLabel("Room number: "), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        name = new JTextField(13);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        name.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 0;
        frame.add(name, gbc);

        phone = new JTextField(13);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        phone.setEditable(true);
        gbc.gridy = 10;
        frame.add(phone, gbc);

        email = new JTextField(13);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        email.setEditable(true);
        gbc.gridy = 20;
        frame.add(email, gbc);

        people = new JTextField(13);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        people.setEditable(true);
        gbc.gridy = 30;
        frame.add(people, gbc);

        fromDate = new DatePicker();
        fromDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        fromDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        fromDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridy = 40;
        frame.add(fromDate, gbc);

        toDate = new DatePicker();
        toDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        toDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        toDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridy = 50;
        frame.add(toDate, gbc);

        rooms = new JComboBox<>(array);
        rooms.setSelectedIndex(0);
        rooms.addActionListener(this::actionPerformed);
        rooms.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridy = 60;
        frame.add(rooms, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        okay = new Button("OK");
        okay.addActionListener(this::actionPerformed);
        gbc.gridx = 0;
        gbc.gridy = 70;
        frame.add(okay, gbc);

        cancelled = new Button("Cancel");
        cancelled.addActionListener(this::actionPerformed);
        gbc.gridx = 5;
        gbc.gridy = 70;
        frame.add(cancelled, gbc);
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
            frame.dispose();
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
            } else {
                int usedPeople = parseInt(people.getText());
                if (MainWindow.timetable.isFree(parseInt(room), from, to)) {
                    reservation = new Reservation(usedName, usedPhone, usedMail, usedPeople, parseInt(room), from, to);
                    MainWindow.frame.setEnabled(true);
                    frame.dispose();
                } else {
                    JOptionPane.showInternalMessageDialog(null, "Room full");
                }
            }
        }
    }
}
