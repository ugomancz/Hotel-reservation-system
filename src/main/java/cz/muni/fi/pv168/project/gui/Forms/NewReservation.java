package cz.muni.fi.pv168.project.gui.Forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.project.gui.Button;
import cz.muni.fi.pv168.project.gui.MainPanel;
import cz.muni.fi.pv168.project.reservations.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class NewReservation extends Form implements ActionListener {
    cz.muni.fi.pv168.project.gui.Button cancelled, okay;
    JTextField name, phone, email, people;
    JComboBox<Integer> rooms;
    DatePicker fromDate, toDate;
    Reservation reservation;
    Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6};


    GridBagConstraints gbc = new GridBagConstraints();

    public NewReservation() {
        super("New Reservation");
        this.setSize(new Dimension(400, 400));
        this.setLayout(new GridBagLayout());
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Name and surname: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(new JLabel("Phone: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 20;
        add(new JLabel("Email: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 30;
        this.add(new JLabel("Number of people: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 40;
        add(new JLabel("From: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 50;
        add(new JLabel("To: "), gbc);

        gbc.gridx = 0;
        gbc.gridy = 60;
        add(new JLabel("Room number: "), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        name = new JTextField(13);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        name.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 0;
        add(name, gbc);

        phone = new JTextField(13);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        phone.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 10;
        add(phone, gbc);

        email = new JTextField(13);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        email.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 20;
        add(email, gbc);

        people = new JTextField(13);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        people.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 30;
        add(people, gbc);

        fromDate = new DatePicker();
        fromDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        fromDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        fromDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 5;
        gbc.gridy = 40;
        add(fromDate, gbc);

        toDate = new DatePicker();
        toDate.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        toDate.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        toDate.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 5;
        gbc.gridy = 50;
        add(toDate, gbc);

        rooms = new JComboBox<>(array);
        rooms.setSelectedIndex(0);
        rooms.addActionListener(this);
        rooms.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 5;
        gbc.gridy = 60;
        add(rooms, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        okay = new Button("OK");
        okay.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 70;
        add(okay, gbc);

        cancelled = new Button("Cancel");
        cancelled.addActionListener(this);
        gbc.gridx = 5;
        gbc.gridy = 70;
        add(cancelled, gbc);


    }

    public Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelled) {
            onClose();
        } else if (e.getSource() == okay) {
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
                if (MainPanel.timetable.isFree(parseInt(room), from, to)) {
                    reservation = new Reservation(usedName, usedPhone, usedMail, usedPeople, parseInt(room), from, to);
                    onClose();
                } else {
                    JOptionPane.showInternalMessageDialog(null, "Room full");
                }
            }
        }
    }
}
