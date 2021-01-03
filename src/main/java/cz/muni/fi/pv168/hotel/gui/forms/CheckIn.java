package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.Guest;
import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.BirthDatePicker;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lukas Hasik
 */

public class CheckIn extends JDialog {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private static final I18N I18N = new I18N(CheckIn.class);
    private final ReservationDao reservationDao;
    private final GuestDao guestDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private JDialog addWindow;
    private JTable table;
    private final ArrayList<Guest> guestList = new ArrayList<Guest>();
    private DefaultTableModel dataModel;
    private JLabel resName, resGuests, resRooms;
    private Button confirm, cancel, add, delete, addConfirm, addCancel;
    private JComboBox<String> reservationPicker;
    private JTextField addNameField, addIDfield;
    private Reservation res;
    private BirthDatePicker birthDatePicker;


    public CheckIn(JFrame frame, ReservationDao reservationDao, GuestDao guestDao) {
        super(frame, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        this.guestDao = guestDao;
        this.reservationDao = reservationDao;
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        setEnabled(true);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        initMap();
        initLayout();
        setVisible(true);

    }


    /**
     * fills map with reservations starting today
     */
    private void initMap() {
        for (Reservation reservation : reservationDao.findAll().stream()
                .filter(x -> x.getArrival().equals(LocalDate.now()))
                .filter(x -> x.getStatus().equals(ReservationStatus.PLANNED))
                .collect(Collectors.toList())) {
            reservationMap.put(reservation.toString(), reservation);
        }
    }


    /**
     * @param dialog JDialog to be adjusted
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
    }

    /**
     * Sets layout in frame using GridBagLayout
     */
    private void initLayout() {
        gbc.weighty = 1;

        JLabel reservation = new JLabel(I18N.getString("reservation") + ": ");
        gbc.anchor = GridBagConstraints.LINE_START;
        placeComponent(this, 0, 0, reservation);
        reservationPicker = new JComboBox<>();
        initComboBox();
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(this, 0, 0, reservationPicker);


        String selected = (String) reservationPicker.getSelectedItem();
        res = reservationMap.get(selected);
        gbc.anchor = GridBagConstraints.LINE_START;
        resName = new JLabel();
        resName.setText(I18N.getString("name") + ": ");
        placeComponent(this, 0, 10, resName);
        resGuests = new JLabel();
        resGuests.setText(I18N.getString("numberOfGuests") + ": ");
        placeComponent(this, 0, 20, resGuests);
        resRooms = new JLabel();
        resRooms.setText(I18N.getString("rooms") + ": ");
        placeComponent(this, 0, 30, resRooms);
        dataModel = new DefaultTableModel() {
            public int getColumnCount() { return 3; }

            private final String[] columns = {I18N.getString("name"), I18N.getString("birthDate"), I18N.getString("IDnumber")};
            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        gbc.anchor = GridBagConstraints.CENTER;
        table = new JTable(dataModel);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(450, 200));
        placeComponent(this, 0, 50, scrollpane);

        gbc.anchor = GridBagConstraints.LINE_START;
        add = new Button(I18N.getString("add"));
        add.setPreferredSize(new Dimension(85, 25));
        add.setEnabled(false);
        placeComponent(this, 0, 40, add);
        add.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 90, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        delete = new Button(I18N.getString("delete"));
        delete.setPreferredSize(new Dimension(100, 25));
        delete.setEnabled(false);
        placeComponent(this, 0, 40, delete);
        delete.addActionListener(this::actionPerformed);


        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        confirm = new Button(I18N.getString("confirm"));
        confirm.setEnabled(false);
        placeComponent(this, 0, 90, confirm);
        confirm.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_END;
        cancel = new Button(I18N.getString("cancel"));
        placeComponent(this, 0, 90, cancel);
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
        addWindow = new JDialog(this, I18N.getString("add"), ModalityType.APPLICATION_MODAL);
        GridBagLayout layout = new GridBagLayout();
        addWindow.setLayout(layout);
        addWindow.setSize(300, 180);
        addWindow.setLocationRelativeTo(this);
        setAddLayout(addWindow);
        addWindow.setVisible(true);
    }

    /**
     *
     * @param addPanel JDialog to be set
     */
    private void setAddLayout(JDialog addPanel) {
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel addNameLabel = new JLabel(I18N.getString("name") + ": ");
        placeComponent(addPanel, 0, 0, addNameLabel);
        JLabel addBirthdateLabel = new JLabel(I18N.getString("birthDate") + ": ");
        placeComponent(addPanel, 0, 1, addBirthdateLabel);
        JLabel addIDLabel = new JLabel(I18N.getString("IDnumber") + ": ");
        placeComponent(addPanel, 0, 2, addIDLabel);

        addNameField = new JTextField(16);
        placeComponent(addPanel, 1, 0, addNameField);
        birthDatePicker = new BirthDatePicker();
        placeComponent(addPanel, 1, 1, birthDatePicker.getPanel());
        addIDfield = new JTextField(16);
        placeComponent(addPanel, 1, 2, addIDfield);

        addConfirm = new Button(I18N.getString("add"));
        addConfirm.setPreferredSize(new Dimension(90, 25));
        addConfirm.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 0, 3, addConfirm);
        gbc.insets = new Insets(0, 75, 0, 0);
        addCancel = new Button(I18N.getString("cancel"));
        addCancel.setPreferredSize(new Dimension(85, 25));
        addCancel.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 1, 3, addCancel);
    }

    private void removeGuest(Guest guest) {
        guestList.removeIf(g -> g.equals(guest));
    }


    /**
     * removes selected rows from a table
     * @param table from which rows shall be deleted
     */
    private void removeSelectedRows(JTable table){
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        int[] rows = table.getSelectedRows();
        for(int i=0;i<rows.length;i++){
            var name = table.getValueAt(rows[i] - i, 0);
            var birthdate = table.getValueAt(rows[i] - i, 1);
            var id = table.getValueAt(rows[i] - i, 2);
            Guest guest = new Guest((String) name, (LocalDate) birthdate, (String) id, res.getId());
            removeGuest(guest);
            model.removeRow(rows[i]-i);
        }
    }

    private void createGuests() {
        for (Guest guest : guestList) {
            guestDao.create(guest);
        }
    }


    private void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cancel)) {
            dispose();
        }
        if (e.getSource().equals(confirm)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            if (res == null) {
                new ErrorDialog(this, I18N.getString("noResSelectedError"));
            } else {
                if (guestList.size() != res.getGuests()) {
                    new ErrorDialog(this, I18N.getString("invalidNumOfGuestsError"));
                } else {
                    createGuests();
                    res.setStatus(ReservationStatus.ONGOING);
                    reservationDao.update(res);
                    Timetable.drawWeek(LocalDate.now());
                    dispose();
                }
            }
        }
        if (e.getSource().equals(reservationPicker)) {
            dataModel.setRowCount(0);
            String selected = (String) reservationPicker.getSelectedItem();
            res = reservationMap.get(selected);
            fillReservation(res);
        }
        if (e.getSource().equals(add)) {
            initAddLayout();
        }
        if (e.getSource().equals(delete)) {
            removeSelectedRows(table);
            confirm.setEnabled(guestList.size() == res.getGuests());
        }
        if (e.getSource().equals(addConfirm)) {
            if (addNameField.getText().equals("") || addIDfield.getText().equals("")) {
                new ErrorDialog(this, I18N.getString("notAllFieldsFilledError"));
            } else {
                String name = addNameField.getText();
                String id = addIDfield.getText();
                Guest guest = new Guest(name, birthDatePicker.getDate(), id, res.getId());
                dataModel.addRow(new Object[] {name, birthDatePicker.getDate(), addIDfield.getText()});
                guestList.add(guest);
                addWindow.dispose();
                confirm.setEnabled(guestList.size() == res.getGuests());

            }
        }
        if (e.getSource().equals(addCancel)) {
            addWindow.dispose();
        }
    }
}