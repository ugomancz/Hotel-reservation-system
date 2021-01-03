package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Kostik
 */
public class GuestsInfo {

    private final JDialog dialog;
    private final GuestDao guestDao;
    private final JComboBox<String> reservationPicker = new JComboBox<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private JTable table;
    private Map<String, Reservation> reservationMap;

    public GuestsInfo(Window owner, ReservationDao reservationDao, GuestDao guestDao) {
        this.reservationDao = reservationDao;
        this.guestDao = guestDao;
        dialog = new JDialog(owner, "Guests", Dialog.ModalityType.APPLICATION_MODAL);
        new LoadReservations().execute();
        initLayout();
        dialog.setVisible(true);
    }

    private void initLayout() {
        dialog.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weighty = 0.5;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel("Reservation:"), 0, 0);
        reservationPicker.addActionListener(this::selectionChanged);
        addComponent(reservationPicker, 1, 0);
        addTable();
        dialog.pack();
    }

    private void selectionChanged(ActionEvent event) {
        String selected = (String) reservationPicker.getSelectedItem();
        Reservation reservation = reservationMap.get(selected);
        displayGuests(reservation);
    }

    private void addComponent(JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void addTable() {
        table = new JTable();
        addComponent(table, 0, 1);
    }

    private void displayGuests(Reservation reservation) {
        clearTable();
        //setTable();
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    private class LoadReservations extends SwingWorker<Map<String, Reservation>, Void> {

        @Override
        protected Map<String, Reservation> doInBackground() {
            Map<String, Reservation> map = new HashMap<>();
            for (Reservation reservation : reservationDao.findAll()) {
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
            for (String reservation : reservationMap.keySet()) {
                reservationPicker.addItem(reservation);
            }
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation reservation = reservationMap.get(selected);
            if (reservation != null) {
                displayGuests(reservation);
            }
        }
    }
}
