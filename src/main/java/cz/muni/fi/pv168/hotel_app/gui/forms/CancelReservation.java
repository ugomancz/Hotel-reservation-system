package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.gui.Timetable;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import cz.muni.fi.pv168.hotel_app.reservations.ReservationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CancelReservation extends JDialog {
    Button cancelButton, okayButton;
    JComboBox<String> reservationPicker;
    JCheckBox confirm;
    Map<String, Reservation> reservationMap = new HashMap<>();
    GridBagConstraints gbc = new GridBagConstraints();
    ReservationDao reservationDao;

    public CancelReservation(ReservationDao reservationDao) {
        super(MainWindow.frame, "Cancel Reservation", ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLocationRelativeTo(MainWindow.frame);
        setMinimumSize(new Dimension(350, 200));
        setLayout(new GridBagLayout());
        initLayout();
        setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void setupComboBox() {
        for (Reservation reservation : reservationDao.findAll()) {
            if (reservation.getStatus().equals(ReservationStatus.PLANNED)) {
                reservationMap.put(reservation.toString(), reservation);
            }
        }
        reservationPicker = new JComboBox<>();
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
    }

    private void addButtons() {
        gbc.anchor = GridBagConstraints.SOUTH;
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 10, okayButton);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(5, 10, cancelButton);
    }

    private void initLayout() {
        setupComboBox();
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.CENTER;

        placeComponent(0, 0, new JLabel("Reservation: "));
        placeComponent(0, 5, new JLabel("Are u sure?"));

        gbc.anchor = GridBagConstraints.LINE_START;

        placeComponent(5, 0, reservationPicker);

        confirm = new JCheckBox();
        confirm.addActionListener(this::actionPerformed);
        placeComponent(5, 5, confirm);
        addButtons();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(okayButton)) {
            if (confirm.isSelected()) {
                String picked = (String) reservationPicker.getSelectedItem();
                reservationDao.delete(reservationMap.get(picked));
                Timetable.drawWeek(LocalDate.now());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Confirmation needed");
            }
        }
    }
}
