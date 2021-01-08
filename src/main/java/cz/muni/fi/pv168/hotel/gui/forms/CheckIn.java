package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.guests.Guest;
import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.BirthDatePicker;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lukas Hasik
 */

public class CheckIn {

    private static final I18N I18N = new I18N(CheckIn.class);
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private final GuestDao guestDao;
    private final RoomDao roomDao;
    private Map<String, Reservation> reservationMap = new HashMap<>();
    private final JDialog dialog;
    private final ArrayList<Guest> guestList = new ArrayList<>();
    private JDialog addWindow, priceWindow;
    private JTable table;
    private JLabel resName, resGuests, resRooms;
    private Button confirm, cancel, add, delete, addConfirm, addCancel, priceConfirm, priceCancel;
    private int price;
    private Integer roomNumber;
    private JComboBox<String> reservationPicker, rooms, prices;
    private JTextField addNameField, addIDfield;
    private Reservation res;
    private BirthDatePicker birthDatePicker;

    public CheckIn(JFrame frame, ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        this.guestDao = guestDao;
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        dialog.setSize(500, 400);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setEnabled(true);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        GridBagLayout layout = new GridBagLayout();
        dialog.setLayout(layout);
        new LoadPlannedReservations().execute();
        initLayout();
        dialog.setVisible(true);

    }


    public CheckIn(JFrame frame, ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao, Reservation reservation) {
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        this.guestDao = guestDao;
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        dialog.setSize(500, 400);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setEnabled(true);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        GridBagLayout layout = new GridBagLayout();
        dialog.setLayout(layout);
        new LoadPlannedReservations().execute();
        initLayout();
        reservationPicker.setSelectedItem(reservation.toString());
        dialog.setVisible(true);

    }

    /**
     * @param dialog    JDialog to be adjusted
     * @param x         coordination for gbc
     * @param y         coordination for gbc
     * @param component to be placed onto frame
     */
    private void placeComponent(JDialog dialog, int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void initComboBox() {
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setPreferredSize(new Dimension(300, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        gbc.anchor = GridBagConstraints.CENTER;
        reservationPicker.setSelectedItem(0);
    }

    private void initRoomComboBox(JComboBox<String> comboBox, Reservation reservation) {
        for (Integer number : reservation.getRoomNumbers()) {
            comboBox.addItem(number.toString());
        }
        comboBox.setPreferredSize(new Dimension(100, 20));
        gbc.anchor = GridBagConstraints.CENTER;
        comboBox.setSelectedItem(0);
    }

    private void initPriceComboBox(JComboBox<String> prices, Integer roomNumber) {
        for (Integer price : Constants.ROOM_PRICES.values()) {
            if (price == roomDao.getPricePerNight(roomNumber)) {
                prices.addItem(price.toString() + ",- " + "(" + I18N.getString("current") + ")");
            } else {
                prices.addItem(price.toString() + ",-");
            }
        }
        prices.addActionListener(this::actionPerformed);
        prices.setPreferredSize(new Dimension(170, 20));
        gbc.anchor = GridBagConstraints.CENTER;
        prices.setSelectedItem(roomDao.getPricePerNight(roomNumber) + ",- " + "(" + I18N.getString("current") + ")");

    }

    /**
     * Sets layout in frame using GridBagLayout
     */
    private void initLayout() {
        gbc.weighty = 1.5;

        JLabel reservation = new JLabel(I18N.getString("reservation") + ":");
        gbc.anchor = GridBagConstraints.LINE_START;
        placeComponent(dialog, 0, 0, reservation);
        reservationPicker = new JComboBox<>();
        initComboBox();
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(dialog, 0, 0, reservationPicker);

        String selected = (String) reservationPicker.getSelectedItem();
        res = reservationMap.get(selected);
        gbc.anchor = GridBagConstraints.LINE_START;
        resName = new JLabel();
        resName.setText(I18N.getString("name") + ":");
        placeComponent(dialog, 0, 10, resName);
        resGuests = new JLabel();
        resGuests.setText(I18N.getString("numberOfGuests") + ":");
        placeComponent(dialog, 0, 20, resGuests);
        resRooms = new JLabel();
        resRooms.setText(I18N.getString("rooms") + ":");
        placeComponent(dialog, 0, 30, resRooms);

        gbc.anchor = GridBagConstraints.CENTER;
        table = GuestTable.createTable();
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(450, 200));
        placeComponent(dialog, 0, 50, scrollpane);

        gbc.anchor = GridBagConstraints.LINE_START;
        add = new Button(I18N.getString("add"));
        add.setPreferredSize(new Dimension(85, 25));
        add.setEnabled(false);
        placeComponent(dialog, 0, 40, add);
        add.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 90, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        delete = new Button(I18N.getString("delete"));
        delete.setPreferredSize(new Dimension(100, 25));
        delete.setEnabled(false);
        placeComponent(dialog, 0, 40, delete);
        delete.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        confirm = new Button(I18N.getString("confirm"));
        confirm.setEnabled(false);
        placeComponent(dialog, 0, 90, confirm);
        confirm.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_END;
        cancel = new Button(I18N.getString("cancel"));
        placeComponent(dialog, 0, 90, cancel);
        cancel.addActionListener(this::actionPerformed);

        if (res != null) {
            fillReservation(res);
            add.setEnabled(true);
            delete.setEnabled(true);
        }

    }

    /**
     * Fills labels with information obtained from selected reservation
     */
    private void fillReservation(Reservation res) {
        resName.setText(I18N.getString("name") + ": " + res.getName());
        resGuests.setText(I18N.getString("numberOfGuests") + ": " + res.getGuests());
        resRooms.setText(I18N.getString("rooms") + ": " + Arrays.toString(res.getRoomNumbers()));
    }

    /**
     * initializes add window
     */
    private void initAddLayout() {
        addWindow = new JDialog(dialog, I18N.getString("add"), Dialog.ModalityType.APPLICATION_MODAL);
        GridBagLayout layout = new GridBagLayout();
        addWindow.setLayout(layout);
        addWindow.setLocationRelativeTo(dialog);
        addWindow.getRootPane().registerKeyboardAction((e) -> addWindow.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setAddLayout(addWindow);
        addWindow.pack();
        addWindow.setResizable(false);
        addWindow.setVisible(true);
    }

    private void initPriceLayout(Integer roomNumber) {
        priceWindow = new JDialog(dialog, I18N.getString("unfilledRoom"), Dialog.ModalityType.APPLICATION_MODAL);
        GridBagLayout layout = new GridBagLayout();
        priceWindow.setLayout(layout);
        priceWindow.setLocationRelativeTo(dialog);
        priceWindow.getRootPane().registerKeyboardAction((e) -> priceWindow.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setPriceLayout(roomNumber);
        priceWindow.pack();
        priceWindow.setResizable(false);
        priceWindow.setVisible(true);
    }

    private void setPriceLayout(Integer roomNumber) {
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        prices = new JComboBox<>();
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel changeOfPrice = new JLabel(I18N.getString("roomNotFilled") + ": " + roomNumber.toString());
        placeComponent(priceWindow, 0, 0, changeOfPrice);
        JLabel choosePrice = new JLabel(I18N.getString("choosePrice") + ": ");
        placeComponent(priceWindow, 0, 1, choosePrice);
        gbc.anchor = GridBagConstraints.CENTER;
        initPriceComboBox(prices, roomNumber);
        placeComponent(priceWindow, 1, 1, prices);

        gbc.anchor = GridBagConstraints.LINE_START;
        priceConfirm = new Button(I18N.getString("confirm"));
        priceConfirm.setPreferredSize(new Dimension(90, 25));
        priceConfirm.addActionListener(this::actionPerformed);
        placeComponent(priceWindow, 0, 2, priceConfirm);

        gbc.anchor = GridBagConstraints.LINE_END;
        priceCancel = new Button(I18N.getString("cancel"));
        priceCancel.setPreferredSize(new Dimension(90, 25));
        priceCancel.addActionListener(this::actionPerformed);
        placeComponent(priceWindow, 1, 2, priceCancel);
    }

    /**
     * @param addPanel JDialog to be set
     */
    private void setAddLayout(JDialog addPanel) {
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel addNameLabel = new JLabel(I18N.getString("name") + ":");
        placeComponent(addPanel, 0, 0, addNameLabel);
        JLabel addBirthdateLabel = new JLabel(I18N.getString("birthDate") + ":");
        placeComponent(addPanel, 0, 1, addBirthdateLabel);
        JLabel addIDLabel = new JLabel(I18N.getString("IDnumber") + ":");
        placeComponent(addPanel, 0, 2, addIDLabel);

        addNameField = new JTextField(16);
        placeComponent(addPanel, 1, 0, addNameField);
        birthDatePicker = new BirthDatePicker();
        placeComponent(addPanel, 1, 1, birthDatePicker.getPanel());
        addIDfield = new JTextField(16);
        addIDfield.setText("-");
        placeComponent(addPanel, 1, 2, addIDfield);


        JLabel roomsLabel = new JLabel(I18N.getString("roomNumber") + ":");
        placeComponent(addPanel, 0, 3, roomsLabel);
        rooms = new JComboBox<>();
        initRoomComboBox(rooms, res);
        gbc.insets = new Insets(5, 5, 5, 70);
        placeComponent(addPanel, 1, 3, rooms);

        gbc.insets = new Insets(5, 5, 5, 5);
        addConfirm = new Button(I18N.getString("add"));
        addConfirm.setPreferredSize(new Dimension(90, 25));
        addConfirm.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 0, 4, addConfirm);
        gbc.insets = new Insets(0, 75, 0, 0);
        addCancel = new Button(I18N.getString("cancel"));
        addCancel.setPreferredSize(new Dimension(85, 25));
        addCancel.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 1, 4, addCancel);
    }

    private void removeGuest(Guest guest) {
        guestList.removeIf(g -> g.equals(guest));
    }

    /**
     * removes selected rows from a table
     *
     * @param table from which rows shall be deleted
     */
    private void removeSelectedRows(JTable table) {
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            var name = table.getValueAt(rows[i] - i, 0);
            var birthdate = table.getValueAt(rows[i] - i, 1);
            var id = table.getValueAt(rows[i] - i, 2);
            Guest guest = new Guest((String) name, (LocalDate) birthdate, (String) id, res.getId());
            removeGuest(guest);
            model.removeRow(rows[i] - i);
        }
    }

    private void createGuests() {
        for (Guest guest : guestList) {
            new CreateGuest(guest).execute();
        }
    }

    private int getNumOfRooms(Integer n) {
        int count = 0;
        for (int i  = 0; i < table.getRowCount(); i++) {

            if (table.getValueAt(i, 3).toString().equals(n.toString())) {
                count++;
            }
        }
        return count;
    }

    private int roomPriceCategoryToInt(RoomDao.RoomPriceCategory category) {
        if (category == RoomDao.RoomPriceCategory.SINGLE_ROOM) {
            return 1;
        }

        if (category == RoomDao.RoomPriceCategory.DOUBLE_ROOM) {
            return 2;
        }

        if (category == RoomDao.RoomPriceCategory.TRIPLE_ROOM) {
            return 3;
        }

        return 4;
    }

    private boolean checkInvalidRooms() {
        for (Integer n : res.getRoomNumbers()) {
            Room room = roomDao.getRoom(n);
            if (getNumOfRooms(n) > roomPriceCategoryToInt(room.getRoomPriceCategory())) {
                return false;
            }
        }
        return true;
    }

    private void checkNonFullRooms() {
        for (Integer n : res.getRoomNumbers()) {
            Room room = roomDao.getRoom(n);
            roomNumber = n;
            if (getNumOfRooms(n) < roomPriceCategoryToInt(room.getRoomPriceCategory())) {
                initPriceLayout(n);
            }
        }
    }

    private void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cancel) | e.getSource().equals(dialog.getRootPane())) {
            dialog.dispose();
        }
        if (e.getSource().equals(confirm)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            if (res == null) {
                new ErrorDialog(dialog, I18N.getString("noResSelectedError"));
            } else {
                if (guestList.size() != res.getGuests()) {
                    new ErrorDialog(dialog, I18N.getString("invalidNumOfGuestsError"));
                } else if (!checkInvalidRooms()) {
                    new ErrorDialog(dialog, I18N.getString("overfilledRoomError"));
                } else {
                    checkNonFullRooms();
                    createGuests();
                    res.setStatus(ReservationStatus.ONGOING);
                    new UpdateReservation(res).execute();
                }
            }
        }
        if (e.getSource().equals(reservationPicker)) {
            DefaultTableModel dataModel = (DefaultTableModel) table.getModel();
            dataModel.setRowCount(0);
            String selected = (String) reservationPicker.getSelectedItem();
            res = reservationMap.get(selected);
            fillReservation(res);
        }
        if (e.getSource().equals(prices)) {
            String selected = (String) prices.getSelectedItem();
            assert selected != null;
            price = Integer.parseInt(selected.split(",")[0]);

        }
        if (e.getSource().equals(add)) {
            initAddLayout();
        }
        if (e.getSource().equals(delete)) {
            removeSelectedRows(table);
            confirm.setEnabled(guestList.size() == res.getGuests());
        }
        if (e.getSource().equals(priceConfirm)) {
            new UpdateReservationPrice(res, roomNumber, price).execute();
        }

        if (e.getSource().equals(addConfirm)) {
            if (addNameField.getText().equals("") || addIDfield.getText().equals("")) {
                new ErrorDialog(dialog, I18N.getString("notAllFieldsFilledError"));
            } else if (birthDatePicker.getDate().isAfter(LocalDate.now())) {
                new ErrorDialog(dialog, I18N.getString("invalidDateError"));
            } else {
                String name = addNameField.getText();
                String id = addIDfield.getText();
                Guest guest = new Guest(name, birthDatePicker.getDate(), id, res.getId());
                DefaultTableModel dataModel = (DefaultTableModel) table.getModel();
                dataModel.addRow(new Object[]{name, birthDatePicker.getDate(), addIDfield.getText(), rooms.getSelectedItem()});
                guestList.add(guest);
                addWindow.dispose();
                confirm.setEnabled(guestList.size() == res.getGuests());
            }
        }
        if (e.getSource().equals(addCancel)) {
            addWindow.dispose();
        }
    }
    private class LoadPlannedReservations extends SwingWorker<Map<String, Reservation>, Void> {

        @Override
        protected Map<String, Reservation> doInBackground() {
            Map<String, Reservation> map = new HashMap<>();
            for (Reservation reservation : reservationDao.findAll().stream()
                    .filter(x -> x.getArrival().equals(LocalDate.now()))
                    .filter(x -> x.getStatus().equals(ReservationStatus.PLANNED))
                    .collect(Collectors.toList())) {
                map.put(reservation.toString(), reservation);
            }
            return map;
        }
        @Override
        public void done() {
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
                fillReservation(reservation);
                add.setEnabled(true);
                delete.setEnabled(true);
            }
        }
    }

    private class UpdateReservationPrice extends SwingWorker<Void, Void> {

        private final Reservation reservation;
        private final Integer roomNumber;
        private final int price;

        private UpdateReservationPrice(Reservation reservation, Integer roomNumber, int price) {
            this.reservation = reservation;
            this.roomNumber = roomNumber;
            this.price = price;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.updatePrice(reservation.getId(), roomNumber, price);
            return null;
        }

        @Override
        protected void done() {
            priceWindow.dispose();
        }
    }

    private class CreateGuest extends SwingWorker<Void, Void> {

        private final Guest guest;

        public CreateGuest(Guest guest) {
            this.guest = guest;
        }


        @Override
        protected Void doInBackground() {
            guestDao.create(guest);
            return null;
        }
    }

    private class UpdateReservation extends SwingWorker<Void, Void> {

        private final Reservation reservation;


        private UpdateReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.update(reservation);
            return null;
        }

        @Override
        protected void done() {
            Timetable.refresh();
            dialog.dispose();
        }
    }
}