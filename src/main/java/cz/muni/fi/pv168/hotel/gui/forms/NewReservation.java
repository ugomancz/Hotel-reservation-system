package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.DesignedDatePicker;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class NewReservation {

    private static final I18N I18N = new I18N(NewReservation.class);
    private final ReservationDao reservationDao;
    private Button cancelButton, okayButton;
    private JTextField name, phone, email, people;
    private JComboBox<Integer> rooms;
    private DesignedDatePicker fromDate, toDate;
    private final Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JDialog dialog;


    public NewReservation(JFrame frame, ReservationDao reservationDao) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setSize(400, 400);

        dialog.setEnabled(true);
        dialog.setLayout(new GridBagLayout());
        dialog.getRootPane().registerKeyboardAction(this::actionPerformed, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        fillOutFrame();
        dialog.setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
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
        fromDate.setFirstAllowedDate(LocalDate.now());
        fromDate.addDateChangeListener(e -> toDate.setFirstAllowedDate(fromDate.getDate().plusDays(1)));
        placeComponent(5, 40, fromDate.getDatePicker());

        toDate = new DesignedDatePicker();
        toDate.setFirstAllowedDate(LocalDate.now().plusDays(1));
        placeComponent(5, 50, toDate.getDatePicker());
    }

    private void addComboBox() {
        rooms = new JComboBox<>(array);
        rooms.setSelectedIndex(0);
        rooms.addActionListener(this::actionPerformed);
        placeComponent(5, 60, rooms);
    }

    private void addButtons() {
        okayButton = new Button(I18N.getString("confirmButton"));
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 70, okayButton);

        cancelButton = new Button(I18N.getString("cancelButton"));
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(5, 70, cancelButton);
    }

    private void fillOutFrame() {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(0, 0, new JLabel(I18N.getString("nameLabel") + ": "));
        placeComponent(0, 10, new JLabel(I18N.getString("phoneLabel") + ": "));
        placeComponent(0, 20, new JLabel(I18N.getString("emailLabel") + ": "));
        placeComponent(0, 30, new JLabel(I18N.getString("guestsLabel") + ": "));
        placeComponent(0, 40, new JLabel(I18N.getString("fromLabel") + ": "));
        placeComponent(0, 50, new JLabel(I18N.getString("toLabel") + ": "));
        placeComponent(0, 60, new JLabel(I18N.getString("roomNumber") + ": "));

        gbc.anchor = GridBagConstraints.LINE_START;
        addFields();
        addDatePickers();
        addComboBox();

        gbc.anchor = GridBagConstraints.SOUTH;
        addButtons();
    }

    private Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        } else if (e.getSource().equals(okayButton)) {
            String usedName = name.getText();
            String usedPhone = phone.getText();
            String usedMail = email.getText();
            String room = Objects.requireNonNull(rooms.getSelectedItem()).toString();
            LocalDate from = fromDate.getDate();
            LocalDate to = toDate.getDate();
            if (usedName.length() == 0) {
                JOptionPane.showMessageDialog(dialog, I18N.getString("nameEmptyError"));
            } else if (usedPhone.length() == 0) {
                JOptionPane.showMessageDialog(dialog, I18N.getString("phoneEmptyError"));
            } else if (tryParse(people.getText()) == null) {
                JOptionPane.showMessageDialog(dialog, I18N.getString("guestsError"));
            } else if (parseInt(people.getText()) > RoomDao.numberOfBeds(parseInt(room))) {
                JOptionPane.showMessageDialog(dialog, "Not enough beds in chosen room");
            } else {
                int usedPeople = parseInt(people.getText());
                if (reservationDao.isFree(parseInt(room), from, to)) {
                    reservationDao.create(new Reservation(usedName, usedPhone, usedMail, usedPeople, parseInt(room), from, to,
                            ReservationStatus.PLANNED.toString()));
                    Timetable.drawWeek(LocalDate.now());
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Room full");
                }
            }
        }
    }
}
