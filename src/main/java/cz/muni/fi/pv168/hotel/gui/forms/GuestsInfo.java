package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.Guest;
import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Kostik
 */
public class GuestsInfo {

    private static final I18N I18N = new I18N(GuestsInfo.class);
    public final GuestDao guestDao;
    private final JDialog dialog;
    private final JComboBox<String> reservationPicker = new JComboBox<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private JTable table;
    private Map<String, Reservation> reservationMap;

    public GuestsInfo(JFrame frame, ReservationDao reservationDao, GuestDao guestDao) {
        this.reservationDao = reservationDao;
        this.guestDao = guestDao;
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setLocationRelativeTo(frame);
        new LoadReservations().execute();
        initLayout();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void initLayout() {
        dialog.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weighty = 0.5;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel(I18N.getString("reservationLabel") + ":"), 0, 0);
        reservationPicker.addActionListener(this::reservationPicked);
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        addComponent(reservationPicker, 1, 0);
        addTable();
        dialog.pack();
    }

    private void reservationPicked(ActionEvent event) {
        String selected = (String) reservationPicker.getSelectedItem();
        Reservation reservation = reservationMap.get(selected);
        if (reservation != null) {
            displayGuests(reservation);
        }
    }

    private void addComponent(JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void addTable() {
        table = GuestTable.createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addComponent(scrollPane, 0, 1);
    }

    private void displayGuests(Reservation reservation) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        new LoadGuests(reservation, model).execute();
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
        }
    }

    private class LoadGuests extends SwingWorker<List<Guest>, Void> {

        private final Reservation reservation;
        private final DefaultTableModel model;

        private LoadGuests(Reservation reservation, DefaultTableModel model) {
            this.reservation = reservation;
            this.model = model;
        }

        @Override
        protected List<Guest> doInBackground() {
            return guestDao.getGuests(reservation.getId());
        }

        @Override
        protected void done() {
            try {
                List<Guest> guests = get();
                for (Guest guest : guests) {
                    model.addRow(new Object[]{guest.getName(), guest.getBirthDate(), guest.getGuestId(), guest.getRoomNumber()});
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
