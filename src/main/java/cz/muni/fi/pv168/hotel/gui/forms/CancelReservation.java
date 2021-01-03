package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timotej Cirok
 */

public class CancelReservation {

    private static final I18N I18N = new I18N(CancelReservation.class);
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private final JDialog dialog;
    private Button cancelButton, okayButton;
    private JComboBox<String> reservationPicker;

    public CancelReservation(JFrame frame, ReservationDao reservationDao) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setLayout(new GridBagLayout());
        dialog.getRootPane().registerKeyboardAction(this::actionPerformed, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        initLayout();
        dialog.setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
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

        gbc.anchor = GridBagConstraints.LINE_START;
        placeComponent(5, 0, reservationPicker);
        addButtons();
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        } else if (e.getSource().equals(okayButton)) {
            String picked = (String) reservationPicker.getSelectedItem();
            if (picked == null) {
                new ErrorDialog(dialog, I18N.getString("reservationError"));
            } else {
                reservationDao.delete(reservationMap.get(picked));
                Timetable.drawWeek(LocalDate.now());
                dialog.dispose();
            }
        }
    }
}
