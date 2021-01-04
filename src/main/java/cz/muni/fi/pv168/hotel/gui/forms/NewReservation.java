package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.*;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * @author Timotej Cirok
 */
public class NewReservation {

    private static final I18N I18N = new I18N(NewReservation.class);
    private final ReservationDao reservationDao;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JDialog dialog;
    private final RoomDao roomDao;
    private final GuestDao guestDao;
    private final JFrame frame;
    private Button cancelButton, checkinButton;
    private JTextField name, phone, email, people;
    private JTable picker;
    private DesignedDatePicker fromDate, toDate;


    public NewReservation(JFrame frame, ReservationDao reservationDao, GuestDao guestDao, RoomDao roomDao) {
        this.frame = frame;
        this.roomDao = roomDao;
        this.guestDao = guestDao;
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        this.reservationDao = reservationDao;
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);

        dialog.setEnabled(true);
        dialog.setLayout(new GridBagLayout());
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        gbc.weightx = 0.5;
        gbc.weighty = 1;
        fillOutFrame();
        dialog.setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private void addFields() {
        name = new JTextField(20);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(1, 0, name);

        phone = new JTextField(20);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(1, 1, phone);

        email = new JTextField(20);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(1, 2, email);

        people = new JTextField(2);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        placeComponent(1, 3, people);
    }

    private void addDatePickers() {
        fromDate = new DesignedDatePicker();
        fromDate.setFirstAllowedDate(LocalDate.now());
        fromDate.setDate(LocalDate.now());
        fromDate.addDateChangeListener(e -> toDate.setFirstAllowedDate(fromDate.getDate().plusDays(1)));
        placeComponent(1, 4, fromDate.getDatePicker());

        toDate = new DesignedDatePicker();
        toDate.setFirstAllowedDate(LocalDate.now().plusDays(1));
        toDate.setDate(LocalDate.now().plusDays(1)) ;
        placeComponent(1, 5, toDate.getDatePicker());
    }

    private void addButtons() {
        gbc.anchor = GridBagConstraints.LINE_START;
        Button okayButton = new Button(I18N.getString("confirmButton"));
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 10, okayButton);

        checkinButton = new Button(I18N.getString("checkinButton"));
        checkinButton.addActionListener(this::actionPerformed);
        placeComponent(1, 10, checkinButton);

        gbc.anchor = GridBagConstraints.LINE_END;
        cancelButton = new Button(I18N.getString("cancelButton"));
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(1, 10, cancelButton);
    }

    private void fillOutFrame() {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        placeComponent(0, 0, new JLabel(I18N.getString("nameLabel") + ": "));
        placeComponent(0, 1, new JLabel(I18N.getString("phoneLabel") + ": "));
        placeComponent(0, 2, new JLabel(I18N.getString("emailLabel") + ": "));
        placeComponent(0, 3, new JLabel(I18N.getString("guestsLabel") + ": "));
        placeComponent(0, 4, new JLabel(I18N.getString("fromLabel") + ": "));
        placeComponent(0, 5, new JLabel(I18N.getString("toLabel") + ": "));

        gbc.anchor = GridBagConstraints.LINE_START;
        addFields();
        addDatePickers();
        addButtons();
        addTable();
        dialog.pack();
    }

    private void addTable() {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        picker = RoomPicker.createTable(roomDao);
        RoomPicker.DesignedTableModel model = (RoomPicker.DesignedTableModel) picker.getModel();
        for (int i = 0; i < roomDao.numberOfRooms(); i++) {
            model.setCellEditable(i, 0, true);
        }
        JScrollPane scrollPane = new JScrollPane(picker);
        scrollPane.setPreferredSize(new Dimension(450, 181));
        gbc.gridwidth = 2;
        placeComponent(0, 6, scrollPane);
    }

    private void actionPerformed(ActionEvent e) {
        boolean flag = true;
        if (e.getSource().equals(cancelButton)) {
            dialog.dispose();
        } else {
            String usedName = name.getText();
            String usedPhone = phone.getText();
            String usedMail = email.getText();
            int checkedPeople = 0;
            ArrayList<Integer> rooms = new ArrayList<>();
            for (int i = 0; i < picker.getRowCount(); i++) {
                if (picker.getValueAt(i, 0).equals(true)) {
                    checkedPeople += roomDao.numberOfBeds(i + 1);
                    rooms.add(i + 1);
                }
            }
            LocalDate from = fromDate.getDate();
            LocalDate to = toDate.getDate();
            if (usedName.length() == 0) {
                new ErrorDialog(dialog, I18N.getString("nameEmptyError"));
            } else if (usedPhone.length() == 0) {
                new ErrorDialog(dialog, I18N.getString("phoneEmptyError"));
            } else if (Validation.isAlpha(usedPhone)){
                new ErrorDialog(dialog,I18N.getString("phoneFormatError"));
            } else if (usedMail.length() != 0 && !Validation.isEmail(usedMail)){
                new ErrorDialog(dialog,I18N.getString("emailFormatError"));
            } else if (!Validation.isNumeric(people.getText())) {
                new ErrorDialog(dialog, I18N.getString("guestsError"));
            } else if (checkedPeople == 0) {
                new ErrorDialog(dialog, I18N.getString("noRoom"));
            } else if (parseInt(people.getText()) > checkedPeople) {
                new ErrorDialog(dialog, I18N.getString("noSpace"));
            } else {
                int usedPeople = parseInt(people.getText());
                for (Integer room : rooms) {
                    if (!reservationDao.isFree(room, from, to)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Integer[] roomNumbers = new Integer[rooms.size()];
                    roomNumbers = rooms.toArray(roomNumbers);
                    Reservation reservation = new Reservation(usedName, usedPhone, usedMail, usedPeople, roomNumbers, from, to,
                            ReservationStatus.PLANNED.toString());
                    if (e.getSource().equals(checkinButton)) { // go straight to check-in
                        //new CheckIn(frame, reservationDao, guestDao, reservation);
                    }
                    new CreateReservation(reservation).execute();
                } else {
                    new ErrorDialog(dialog, I18N.getString("roomFull"));
                }
            }
        }
    }

    private class CreateReservation extends SwingWorker<Void, Void> {

        private final Reservation reservation;

        public CreateReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Void doInBackground() {
            reservationDao.create(reservation);
            return null;
        }

        @Override
        public void done() {
            Timetable.refresh();
            dialog.dispose();
        }
    }
}
