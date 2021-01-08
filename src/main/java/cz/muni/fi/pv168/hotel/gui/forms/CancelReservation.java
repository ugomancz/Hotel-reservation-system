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
import javax.swing.SwingWorker;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Timotej Cirok
 */

public class CancelReservation {

    private static final I18N I18N = new I18N(CancelReservation.class);
    private static Map<String, Reservation> reservationMap = new HashMap<>();
    private static ReservationDao reservationDao;
    private static JDialog dialog;
    private static Button cancelButton, okayButton;
    private static JComboBox<String> reservationPicker;
    private final GridBagConstraints gbc = new GridBagConstraints();

    public CancelReservation(JFrame frame, ReservationDao reservationDao) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        CancelReservation.reservationDao = reservationDao;
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setLayout(new GridBagLayout());
        dialog.getRootPane().registerKeyboardAction(CancelReservation::actionPerformed, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        initLayout();
        dialog.setVisible(true);
    }

    private static void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        } else if (e.getSource().equals(okayButton)) {
            String picked = (String) reservationPicker.getSelectedItem();
            if (picked == null) {
                new ErrorDialog(dialog, I18N.getString("reservationError"));
            } else {
                new DeleteReservation(reservationMap.get(picked)).execute();
            }
        }
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void addButtons() {
        gbc.anchor = GridBagConstraints.SOUTH;
        okayButton = new Button(I18N.getString("confirmButton"));
        okayButton.addActionListener(CancelReservation::actionPerformed);
        placeComponent(0, 10, okayButton);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        cancelButton = new Button(I18N.getString("cancelButton"));
        cancelButton.addActionListener(CancelReservation::actionPerformed);
        placeComponent(5, 10, cancelButton);
    }

    private void initLayout() {
        new LoadReservations().execute();
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.CENTER;

        placeComponent(0, 0, new JLabel(I18N.getString("reservation") + ": "));
        addButtons();
    }

    private static class DeleteReservation extends SwingWorker<Void, Void> {

        private final Reservation reservation;

        public DeleteReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.delete(reservation);
            return null;
        }

        @Override
        public void done() {
            try {
                get();
                Timetable.refresh();
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class LoadReservations extends SwingWorker<Map<String, Reservation>, Void> {

        @Override
        protected Map<String, Reservation> doInBackground() {
            Map<String, Reservation> map = new HashMap<>();
            for (Reservation reservation : reservationDao.findAll().stream()
                    .filter((x) -> x.getStatus() == ReservationStatus.PLANNED)
                    .collect(Collectors.toList())) {
                map.put(reservation.toString(), reservation);
            }
            return map;
        }

        @Override
        protected void done() {
            try {
                reservationMap = get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            reservationPicker = new JComboBox<>();
            try {
                for (String name : get().keySet()) {
                    reservationPicker.addItem(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            reservationPicker.setPreferredSize(new Dimension(223, 20));
            reservationPicker.addActionListener(CancelReservation::actionPerformed);
            gbc.anchor = GridBagConstraints.LINE_START;
            placeComponent(5, 0, reservationPicker);
            dialog.pack();
        }
    }
}
