package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.Room;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class Reorganisation {

    private static final I18N I18N = new I18N(Reorganisation.class);
    private final ReservationDao reservationDao;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private JDialog dialog;
    private Map<String, Reservation> reservationMap;
    private Button okButton, cancelButton;
    private JComboBox<Integer> oldRoom  = new JComboBox<>();
    private JComboBox<Integer> newRoom  = new JComboBox<>();
    private JComboBox<String> reservationPicker;

    public Reorganisation(JFrame frame, ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
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
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        addComponent(new JLabel(I18N.getString("reservationLabel")), 0, 0);
        addComponent(new JLabel(I18N.getString("oldRoomLabel")), 0, 1);
        addComponent(oldRoom, 1, 1);
        addComponent(new JLabel(I18N.getString("newRoomLabel")), 2, 1);
        addComponent(newRoom, 3, 1);
    }

    private void addComponent(Component component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
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
                return;
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
            gbc.gridwidth = 3;
            reservationPicker.addActionListener(Reorganisation::reservationPicked);
            addComponent(reservationPicker, 1, 0);
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private static void reservationPicked(ActionEvent event) {
    }
}
