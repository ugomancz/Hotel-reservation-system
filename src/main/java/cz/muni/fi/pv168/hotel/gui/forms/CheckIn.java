package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.Guest;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lukas Hasik
 */

public class CheckIn extends JDialog {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private JDialog addWindow;
    private ArrayList<Guest> guestList = new ArrayList();
    private DefaultTableModel dataModel;
    private JLabel resName, resGuests;
    private Button confirm, cancel, add, delete, addConfirm, addCancel;
    private JComboBox<String> reservationPicker;
    private JTextField addNameField, addBirthdateField, addIDfield;
    private Reservation res;


    public CheckIn(JFrame frame, ReservationDao reservationDao) {
        super(frame, "Check-in", ModalityType.APPLICATION_MODAL);
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

        reservationPicker = new JComboBox<>();
        initComboBox();
        placeComponent(this, 0, 0, reservationPicker);


        String selected = (String) reservationPicker.getSelectedItem();
        res = reservationMap.get(selected);
        if (res != null) {
            gbc.anchor = GridBagConstraints.LINE_START;
            resName = new JLabel();
            resName.setText("Name and surname: " + res.getName());
            placeComponent(this, 0, 10, resName);
            resGuests = new JLabel();
            resGuests.setText("Number of guests: " + res.getGuests());
            placeComponent(this, 0, 20, resGuests);
        }
        dataModel = new DefaultTableModel() {
            public int getColumnCount() { return 3; }

            private final String[] columns = {"Name and Surname", "Birth-date", "ID number"};
            @Override
            public String getColumnName(int column) {
                return columns[column];
            }
        };
        gbc.anchor = GridBagConstraints.CENTER;
        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(450, 200));
        placeComponent(this, 0, 40, scrollpane);

        gbc.anchor = GridBagConstraints.LINE_START;
        add = new Button("Add");
        add.setPreferredSize(new Dimension(65, 25));
        placeComponent(this, 0, 30, add);
        add.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 70, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        delete = new Button("Delete");
        delete.setPreferredSize(new Dimension(80, 25));
        placeComponent(this, 0, 30, delete);
        delete.addActionListener(this::actionPerformed);


        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        confirm = new Button("Confirm");
        placeComponent(this, 0, 80, confirm);
        confirm.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_END;
        cancel = new Button("Cancel");
        placeComponent(this, 0, 80, cancel);
        cancel.addActionListener(this::actionPerformed);

    }

    /**
     * Fills labels with information obtained from selected reservation
     */
    private void fillReservation(Reservation res) {
        resName.setText("Name: " + res.getName());
        resGuests.setText("Number of guests: " + res.getGuests());
    }

    private void initAddLayout() {
        addWindow = new JDialog(this, "Add", ModalityType.APPLICATION_MODAL);
        GridBagLayout layout = new GridBagLayout();
        addWindow.setLayout(layout);
        addWindow.setSize(300, 180);
        addWindow.setLocationRelativeTo(this);
        setAddLayout(addWindow);
        addWindow.setVisible(true);
    }

    private void setAddLayout(JDialog addPanel) {
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel addNameLabel = new JLabel("Name: ");
        placeComponent(addPanel, 0, 0, addNameLabel);
        JLabel addBirthdateLabel = new JLabel("Birthdate: ");
        placeComponent(addPanel, 0, 1, addBirthdateLabel);
        JLabel addIDLabel = new JLabel("ID: ");
        placeComponent(addPanel, 0, 2, addIDLabel);

        addNameField = new JTextField(16);
        placeComponent(addPanel, 1, 0, addNameField);
        addBirthdateField = new JTextField(16);
        placeComponent(addPanel, 1, 1, addBirthdateField);
        addIDfield = new JTextField(16);
        placeComponent(addPanel, 1, 2, addIDfield);

        addConfirm = new Button("Confirm");
        addConfirm.setPreferredSize(new Dimension(90, 25));
        addConfirm.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 0, 3, addConfirm);
        gbc.insets = new Insets(0, 75, 0, 0);
        addCancel = new Button("Cancel");
        addCancel.setPreferredSize(new Dimension(85, 25));
        addCancel.addActionListener(this::actionPerformed);
        placeComponent(addPanel, 1, 3, addCancel);
    }


    private void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cancel)) {
            dispose();
        }
        if (e.getSource().equals(confirm)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            if (res == null) {
                new ErrorDialog(this, "No reservation selected");

            } else {
                res.setStatus(ReservationStatus.ONGOING);
                reservationDao.update(res);
                Timetable.drawWeek(LocalDate.now());
                dispose();
            }
        }
        if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            res = reservationMap.get(selected);
            fillReservation(res);
        }
        if (e.getSource().equals(add)) {
            initAddLayout();
        }
        if (e.getSource().equals(addConfirm)) {
            if (addNameField.getText().equals("") || addBirthdateField.getText().equals("") || addIDfield.getText().equals("")) {
                new ErrorDialog(this, "All fields must be filled");
            } else {
                String name = addNameField.getText();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd. MM. yyyy");
                LocalDate birthDate = LocalDate.parse(addBirthdateField.getText(), df);
                String id = addIDfield.getText();
                Guest guest = new Guest(addNameField.getText(), birthDate, id, res.getId());
                dataModel.addRow(new Object[] {addNameField.getText(), addBirthdateField.getText(), addIDfield.getText()});
                guestList.add(guest);
                addWindow.dispose();

            }
        }
        if (e.getSource().equals(addCancel)) {
            addWindow.dispose();
        }
    }
}