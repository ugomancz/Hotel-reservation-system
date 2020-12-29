package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.Guest;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lukas Hasik
 */

public class CheckIn extends JDialog {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private final ReservationDao reservationDao;
    private final Map<String, Reservation> reservationMap = new HashMap<>();
    private List guestList = new ArrayList<Guest>();
    private JTable table = new JTable();
    private JLabel nameLabel, phoneLabel, emailLabel, guestLabel, roomLabel, lengthLabel;
    private Button confirm, cancel, add, delete;
    private JComboBox<String> reservationPicker;
    private JTextField idTextField;


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
    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
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
        placeComponent(0, 0, reservationPicker);


        String selected = (String) reservationPicker.getSelectedItem();
        Reservation res = reservationMap.get(selected);
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel resName = new JLabel();
        resName.setText("Name and surname: " + res.getName());
        placeComponent(0, 10, resName);
        JLabel resGuests = new JLabel();
        resGuests.setText("Number of guests: " + res.getGuests());
        placeComponent(0, 20, resGuests);


        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { return 3; }

            private final String[] columns = {"Name and Surname", "Birth-date", "ID number"};
            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            public int getRowCount() { return res.getGuests();}
            public Object getValueAt(int row, int col) { return null; }
        };
        gbc.anchor = GridBagConstraints.CENTER;
        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(450, 200));
        placeComponent(0, 40, scrollpane);

        gbc.anchor = GridBagConstraints.LINE_START;
        add = new Button("Add");
        add.setPreferredSize(new Dimension(65, 20));
        placeComponent(0, 30, add);
        add.addActionListener(this::actionPerformed);

        gbc.insets = new Insets(0, 70, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        delete = new Button("Delete");
        delete.setPreferredSize(new Dimension(80, 20));
        placeComponent(0, 30, delete);
        delete.addActionListener(this::actionPerformed);






        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        confirm = new Button("Confirm");
        placeComponent(0, 80, confirm);
        confirm.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_END;
        cancel = new Button("Cancel");
        placeComponent(0, 80, cancel);
        cancel.addActionListener(this::actionPerformed);

    }

    /**
     * Fills labels with information obtained from selected reservation
     */
    private void fillReservation(Reservation res) {
        nameLabel.setText("Name and Surname: " + res.getName());
        phoneLabel.setText("Phone number: " + res.getPhone());
        emailLabel.setText("Email: " + res.getEmail());
        guestLabel.setText("Number of guests: " + res.getGuests());
        lengthLabel.setText("Length of stay: " + res.getLength() + " nights");
        roomLabel.setText("Room number: " + res.getRoomNumber());
    }


    private void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cancel)) {
            dispose();
        }
        if (e.getSource().equals(confirm)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            res.setStatus(ReservationStatus.ONGOING);
            if (idTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Guest ID can't be empty");
            } else {
                res.setGuestID(idTextField.getText());
                reservationDao.update(res);
                Timetable.drawWeek(LocalDate.now());
                dispose();
            }
        }
        if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            Reservation res = reservationMap.get(selected);
            fillReservation(res);
        }

        if (e.getSource().equals(add)) {
            JDialog addWindow = new JDialog(this, "Add", ModalityType.APPLICATION_MODAL);
            addWindow.setSize(300, 300);
            addWindow.setLocationRelativeTo(this);
            addWindow.setVisible(true);
        }
    }
}