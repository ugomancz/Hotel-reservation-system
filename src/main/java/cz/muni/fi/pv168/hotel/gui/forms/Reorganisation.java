package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

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
    private final RoomDao roomDao;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JDialog dialog;
    private Map<String, Reservation> reservationMap;
    private Button okButton;
    private final JComboBox<Integer> oldRoom = new JComboBox<>();
    private final JComboBox<Integer> newRoom = new JComboBox<>();
    private JComboBox<String> reservationPicker;

    public Reorganisation(JFrame frame, ReservationDao reservationDao, RoomDao roomDao) {
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setLocationRelativeTo(frame);
        new LoadReservations().execute();
        initLayout();
        dialog.setResizable(false);
    }

    private void initLayout() {
        dialog.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        addComponent(new JLabel(I18N.getString("reservationLabel")), 0, 0);
        addComponent(new JLabel(I18N.getString("oldRoomLabel")), 0, 1);
        addComponent(new JLabel(I18N.getString("newRoomLabel")), 2, 1);

        okButton = new Button(I18N.getString("okButton"), this::actionPerformed);
        gbc.anchor = GridBagConstraints.LINE_START;
        addComponent(okButton, 0, 2);

        Button cancelButton = new Button(I18N.getString("cancelButton"), (e) -> dialog.dispose());
        gbc.anchor = GridBagConstraints.LINE_END;
        addComponent(cancelButton, 3, 2);

        initComboBoxes();
    }

    private void initComboBoxes() {
        oldRoom.addActionListener(this::actionPerformed);
        oldRoom.setPreferredSize(new Dimension(50, 20));
        addComponent(oldRoom, 1, 1);
        newRoom.setPreferredSize(new Dimension(50, 20));
        addComponent(newRoom, 3, 1);
        reservationPicker = new JComboBox<>();
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addComponent(reservationPicker, 1, 0);
    }

    private void actionPerformed(ActionEvent e) {
        String selected = (String) reservationPicker.getSelectedItem();
        if (e.getSource().equals(reservationPicker)) {
            oldRoom.removeAllItems();
            new LoadReservationRooms(reservationMap.get(selected)).execute();
        } else if (e.getSource().equals(oldRoom)) {
            newRoom.removeAllItems();
            new LoadFreeRooms(reservationMap.get(selected)).execute();
        } else if (e.getSource().equals(okButton)) {
            if (selected == null) {
                new ErrorDialog(dialog, I18N.getString("reservationError"));
            }
            Integer oldRoomNumber = (Integer) oldRoom.getSelectedItem();
            Integer newRoomNumber = (Integer) newRoom.getSelectedItem();
            new UpdateRoomNumber(oldRoomNumber, newRoomNumber, reservationMap.get(selected)).execute();
        }
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
            try {
                for (String name : get().keySet()) {
                    reservationPicker.addItem(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private class LoadReservationRooms extends SwingWorker<Integer[], Void> {

        private final Reservation reservation;

        public LoadReservationRooms(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Integer[] doInBackground() {
            return reservationDao.getReservationRoomNumbers(reservation.getId());
        }

        @Override
        protected void done() {
            try {
                for (Integer roomNumber : get()) {
                    oldRoom.addItem(roomNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadFreeRooms extends SwingWorker<List<Room>, Void> {

        private final Reservation reservation;

        public LoadFreeRooms(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected List<Room> doInBackground() {
            return reservationDao.getFreeRooms(reservation.getArrival(), reservation.getDeparture(), roomDao);
        }

        @Override
        protected void done() {
            try {
                for (Room room : get()) {
                    newRoom.addItem(room.getRoomNumber());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateRoomNumber extends SwingWorker<Void, Void> {

        private final Integer oldRoomNumber;
        private final Integer newRoomNumber;
        private final Reservation reservation;

        public UpdateRoomNumber(Integer oldRoomNumber, Integer newRoomNumber, Reservation reservation) {
            this.oldRoomNumber = oldRoomNumber;
            this.newRoomNumber = newRoomNumber;
            this.reservation = reservation;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.updateRoomNumber(reservation.getId(), oldRoomNumber, newRoomNumber);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Timetable.refresh();
            dialog.dispose();
        }
    }
}
