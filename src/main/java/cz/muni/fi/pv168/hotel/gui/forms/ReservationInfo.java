package cz.muni.fi.pv168.hotel.gui.forms;

import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.DesignedDatePicker;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.gui.Validation;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class ReservationInfo {

    private static final I18N I18N = new I18N(ReservationInfo.class);
    private final ReservationDao reservationDao;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final DesignedDatePicker arrival = new DesignedDatePicker();
    private final DesignedDatePicker departure = new DesignedDatePicker();
    private final JDialog dialog;
    private final RoomDao roomDao;
    private final Map<Integer, Integer> roomIndex = Map.ofEntries(
            Map.entry(101, 0),
            Map.entry(102, 1),
            Map.entry(103, 2),
            Map.entry(104, 3),
            Map.entry(105, 4),
            Map.entry(106, 5),
            Map.entry(201, 6),
            Map.entry(202, 7),
            Map.entry(203, 8),
            Map.entry(204, 9),
            Map.entry(205, 10),
            Map.entry(301, 11),
            Map.entry(302, 12),
            Map.entry(303, 13)
    );
    private Map<String, Reservation> reservationMap;
    private Button cancelButton, confirmButton;
    private JTextField nameField, phoneField, emailField, guestsField;
    private JTable roomPicker;
    private JComboBox<String> reservationPicker;

    public ReservationInfo(JFrame frame, ReservationDao reservationDao, RoomDao roomDao) {
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        dialog = new JDialog(frame, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.getRootPane().registerKeyboardAction(this::actionPerformed, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        new LoadReservations().execute();
        initLayout();
        dialog.setMinimumSize(new Dimension(480, 500));
        dialog.pack();
        dialog.setVisible(true);
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel(I18N.getString("reservationLabel") + ":"), 0, 0);
        addComponent(new JLabel(I18N.getString("nameLabel") + ":"), 0, 1);
        addComponent(new JLabel(I18N.getString("phoneLabel") + ":"), 0, 2);
        addComponent(new JLabel(I18N.getString("emailLabel") + ":"), 0, 3);
        addComponent(new JLabel(I18N.getString("guestsLabel") + ":"), 0, 4);
        addComponent(new JLabel(I18N.getString("fromLabel") + ":"), 0, 5);
        addComponent(new JLabel(I18N.getString("toLabel") + ":"), 0, 6);
        addButtons();

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 5, 5, 5);
        nameField = (JTextField) addComponent(new JTextField(20), 1, 1);
        phoneField = (JTextField) addComponent(new JTextField(20), 1, 2);
        emailField = (JTextField) addComponent(new JTextField(20), 1, 3);
        guestsField = (JTextField) addComponent(new JTextField(2), 1, 4);
        addDatePickers();
        addComboBoxes();
        addTable();
    }

    private void addDatePickers() {
        arrival.setFirstAllowedDate(LocalDate.now());
        addComponent(arrival.getDatePicker(), 1, 5);

        departure.setFirstAllowedDate(LocalDate.now().plusDays(1));
        addComponent(departure.getDatePicker(), 1, 6);
    }

    private JComponent addComponent(JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
        return component;
    }

    private void addButtons() {
        gbc.anchor = GridBagConstraints.LINE_START;
        confirmButton = new Button(I18N.getString("confirmButton"), this::actionPerformed);
        confirmButton.setEnabled(false);
        addComponent(confirmButton, 0, 8);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 10, 0, 10);
        cancelButton = new Button(I18N.getString("cancelButton"), this::actionPerformed);
        addComponent(cancelButton, 1, 8);
    }

    private void addComboBoxes() {
        reservationPicker = new JComboBox<>();
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        addComponent(reservationPicker, 1, 0);
    }

    private void addTable() {
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        roomPicker = RoomPicker.createTable(roomDao);
        roomPicker.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(roomPicker);
        scrollPane.setPreferredSize(new Dimension(450, 181));
        addComponent(scrollPane, 0, 7);
    }

    private void addDateChangeListeners() {
        arrival.addDateChangeListener(this::arrivalPicked);
        departure.addDateChangeListener(this::departurePicked);
    }

    private void arrivalPicked(DateChangeEvent event) {
        departure.setFirstAllowedDate(arrival.getDate().plusDays(1));
        if (departure.getDate() == null || departure.getDate().isBefore(arrival.getDate())) {
            departure.setDate(arrival.getDate().plusDays(1));
        }
        if (arrival.getDate() != null && departure.getDate() != null && getSelectedReservation() != null) {
            new UpdateTable().execute();
        }
    }

    private void departurePicked(DateChangeEvent event) {
        if (arrival.getDate() == null || departure.getDate().isBefore(arrival.getDate())) {
            arrival.setDate(departure.getDate().minusDays(1));
        }
        if (arrival.getDate() != null && departure.getDate() != null && getSelectedReservation() != null) {
            new UpdateTable().execute();
        }
    }

    private Reservation getSelectedReservation() {
        String selected = (String) reservationPicker.getSelectedItem();
        return reservationMap.get(selected);
    }

    private void displayInfo(Reservation reservation) {
        nameField.setText(reservation.getName());
        phoneField.setText(reservation.getPhone());
        emailField.setText(reservation.getEmail());
        guestsField.setText(Integer.toString(reservation.getGuests()));
        arrival.setDate(reservation.getArrival());
        departure.setDate(reservation.getDeparture());
        clearSelectedRooms();
        setSelectedRooms(reservation.getRoomNumbers());
    }

    private boolean updateReservation(Reservation reservation) {
        Integer[] rooms = getSelectedRooms();
        if (checkEmptyFields()) {
            showError(I18N.getString("fieldsError"));
            return false;
        } else if (!Validation.isAlpha(nameField.getText())) {
            showError(I18N.getString("nameError"));
            return false;
        } else if (Validation.isAlpha(phoneField.getText())) {
            showError(I18N.getString("phoneError"));
            return false;
        } else if (!emailField.getText().equals("") && Validation.isNotEmail(emailField.getText())) {
            showError(I18N.getString("emailError"));
            return false;
        } else if (Validation.isNotNumeric(guestsField.getText())) {
            showError(I18N.getString("guestsError"));
            return false;
        } else if (roomDao.numberOfBeds(rooms) < Integer.parseInt(guestsField.getText())) {
            showError(I18N.getString("bedError"));
            return false;
        } else if (arrival.getDate() == null || !departure.getDate().isAfter(arrival.getDate())) {
            showError(I18N.getString("dateError"));
            return false;
        }
        reservation.setGuests(Integer.parseInt(guestsField.getText()));
        reservation.setName(nameField.getText());
        reservation.setPhone(phoneField.getText());
        reservation.setEmail(emailField.getText());
        reservation.setArrival(arrival.getDate());
        reservation.setDeparture(departure.getDate());
        reservation.setRoomNumbers(getSelectedRooms());
        new UpdateReservation(reservation).execute();
        return true;
    }

    private boolean checkEmptyFields() {
        try {
            arrival.getDate();
            departure.getDate();
        } catch (NullPointerException ex) {
            return false;
        }
        return !(!nameField.getText().equals("") && !phoneField.getText().equals("") && getSelectedRooms().length != 0);
    }

    private Integer[] getSelectedRooms() {
        Map<Integer, Integer> reversedRoomIndex = roomIndex.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < roomPicker.getRowCount(); i++) {
            if ((boolean) roomPicker.getValueAt(i, 0)) {
                selected.add(reversedRoomIndex.get((i)));
            }
        }
        return selected.toArray(Integer[]::new);
    }

    private void setSelectedRooms(Integer[] roomNumbers) {
        for (Integer roomNumber : roomNumbers) {
            roomPicker.setValueAt(true, roomIndex.get(roomNumber), 0);
        }
    }

    private void clearSelectedRooms() {
        for (int i = 0; i < roomPicker.getRowCount(); i++) {
            roomPicker.setValueAt(false, i, 0);
        }
    }

    private void showError(String error) {
        new ErrorDialog(dialog, error);
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        } else if (e.getSource().equals(confirmButton)) {
            if (getSelectedReservation() == null) {
                showError(I18N.getString("reservationError"));
            } else {
                if (updateReservation(getSelectedReservation())) {
                    dialog.dispose();
                }
            }
        } else if (e.getSource().equals(reservationPicker)) {
            displayInfo(getSelectedReservation());
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
            for (String reservation : reservationMap.keySet()) {
                reservationPicker.addItem(reservation);
            }
            Reservation reservation = getSelectedReservation();
            addDateChangeListeners();
            if (reservation != null) {
                displayInfo(reservation);
                new UpdateTable().execute();
            }
            confirmButton.setEnabled(true);
        }
    }

    private class UpdateReservation extends SwingWorker<Void, Void> {

        private final Reservation reservation;

        public UpdateReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.update(reservation);
            return null;
        }

        @Override
        public void done() {
            Timetable.refresh();
            dialog.dispose();
        }
    }

    private class UpdateTable extends SwingWorker<List<Room>, Void> {

        @Override
        protected List<Room> doInBackground() {
            Reservation reservation = getSelectedReservation();
            try {
                return reservationDao.getFreeRooms(arrival.getDate(), departure.getDate(), roomDao, reservation.getId());
            } catch (NullPointerException ex) {
                return reservationDao.getFreeRooms(reservation.getArrival(), departure.getDate(), roomDao, reservation.getId());
            }
        }

        @Override
        protected void done() {
            RoomPicker.DesignedTableModel model = (RoomPicker.DesignedTableModel) roomPicker.getModel();
            try {
                for (int i = 0; i < roomDao.numberOfRooms(); i++) {
                    model.setCellEditable(i, 0, false);
                }
                for (Integer roomIndex : get().stream()
                        .map((x) -> roomIndex.get(x.getRoomNumber())).toArray(Integer[]::new)) {
                    model.setCellEditable(roomIndex, 0, true);
                }
                for (int i = 0; i < roomDao.numberOfRooms(); i++) {
                    if (!model.isCellEditable(i, 0) && (boolean) roomPicker.getValueAt(i, 0)) {
                        roomPicker.setValueAt(false, i, 0);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
