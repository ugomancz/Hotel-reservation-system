package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CancelReservation extends JDialog {

    private static final I18N I18N = new I18N(CancelReservation.class);
    Button cancelButton, okayButton;
    JComboBox<String> reservationPicker;
    JCheckBox confirm;
    Map<String, Reservation> reservationMap = new HashMap<>();
    GridBagConstraints gbc = new GridBagConstraints();
    ReservationDao reservationDao;

    public CancelReservation(JFrame frame, ReservationDao reservationDao) {
        super(frame, I18N.getString("title"), ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        setLocationRelativeTo(frame);
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
        okayButton = new Button(I18N.getString("confirmButton"));
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 10, okayButton);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        cancelButton = new Button(I18N.getString("cancelButton"));
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(5, 10, cancelButton);
    }

    private void initLayout() {
        setupComboBox();
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.CENTER;

        placeComponent(0, 0, new JLabel(I18N.getString("reservation") + ": "));
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
