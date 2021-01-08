package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.Button;
import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.gui.Timetable;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Ondrej Kostik
 */
public class CheckOut {

    private static final I18N I18N = new I18N(CheckOut.class);
    private final ReservationDao reservationDao;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JDialog dialog;
    private final JTextArea textArea = new JTextArea();
    private int localFee;
    private Map<String, Reservation> reservationMap;
    private JButton outButton, cancelButton;
    private JComboBox<String> reservationPicker;

    public CheckOut(Window owner, ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        dialog = new JDialog(owner, I18N.getString("windowTitle"), ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(owner);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        new LoadOngoingReservations().execute();
        new LoadProperties().execute();
        initLayout();
        dialog.setVisible(true);
    }

    private void initLayout() {
        dialog.setLayout(new GridBagLayout());
        dialog.setMinimumSize(new Dimension(250, 250));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        addComponent(addComboBox(), 0);
        textArea.setEditable(false);
        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(215, 200));
        addComponent(pane, 1);
        addButtons();
        dialog.pack();
    }

    private void addButtons() {
        outButton = new Button(I18N.getString("confirmButton"));
        outButton.addActionListener(this::actionPerformed);
        outButton.setEnabled(false);
        cancelButton = new Button(I18N.getString("cancelButton"));
        cancelButton.addActionListener(this::actionPerformed);

        gbc.anchor = GridBagConstraints.LINE_START;
        addComponent(outButton, 2);
        gbc.anchor = GridBagConstraints.LINE_END;
        addComponent(cancelButton, 2);
    }

    private void addComponent(JComponent component, int y) {
        gbc.gridy = y;
        dialog.add(component, gbc);
    }

    private JComboBox<String> addComboBox() {
        reservationPicker = new JComboBox<>();
        reservationPicker.setPreferredSize(new Dimension(220, 22));
        reservationPicker.addActionListener(this::actionPerformed);
        return reservationPicker;
    }

    private int calculateTotalPrice(Reservation reservation, Map<Integer, Integer> roomMap) {
        int length = LocalDate.now().compareTo(reservation.getArrival());
        int pricePerNight = roomMap.values().stream().mapToInt(Integer::intValue).sum();
        return length * pricePerNight + length * localFee * reservation.getGuests();
    }

    private void displayInfo(Reservation reservation, Map<Integer, Integer> roomMap) {
        textArea.setText("");
        textArea.append(I18N.getString("clientLabel") + ": " + reservation.getName() + "\n");
        for (Integer roomNumber : roomMap.keySet()) {
            textArea.append(I18N.getString("roomNumberLabel") + " " + roomNumber + ": " + roomMap.get(roomNumber) + "\n");
        }
        textArea.append(I18N.getString("feesLabel") + ": " + localFee + "\n");
        textArea.append(I18N.getString("nightsLabel") + ": " + LocalDate.now().compareTo(reservation.getArrival()) + "\n");
        textArea.append(I18N.getString("guestsLabel") + ": " + reservation.getGuests() + "\n");
        textArea.append(I18N.getString("totalLabel") + ": " + calculateTotalPrice(reservation, roomMap));
    }

    private void closeReservation(Reservation reservation) {
        if (reservation == null) {
            new ErrorDialog(dialog, I18N.getString("selectionError"));
        } else {
            reservation.setDeparture(LocalDate.now());
            reservation.setStatus(ReservationStatus.PAST);
            new UpdateReservation(reservation).execute();
        }
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outButton)) {
            String selected = (String) reservationPicker.getSelectedItem();
            closeReservation(reservationMap.get(selected));
        } else if (e.getSource().equals(reservationPicker)) {
            String selected = (String) reservationPicker.getSelectedItem();
            new CalculatePrice(reservationMap.get(selected)).execute();
        } else if (e.getSource().equals(cancelButton)) {
            dialog.dispose();
        }
    }

    private class LoadOngoingReservations extends SwingWorker<Map<String, Reservation>, Void> {

        @Override
        protected Map<String, Reservation> doInBackground() {
            Map<String, Reservation> map = new HashMap<>();
            for (Reservation reservation : reservationDao.findAll().stream()
                    .filter((x) -> x.getStatus() == ReservationStatus.ONGOING)
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
                new CalculatePrice(reservation).execute();
            }
            outButton.setEnabled(true);
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
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Timetable.drawWeek(LocalDate.now());
            dialog.dispose();
        }
    }

    private class CalculatePrice extends SwingWorker<Map<Integer, Integer>, Void> {

        private final Reservation reservation;

        private CalculatePrice(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        protected Map<Integer, Integer> doInBackground() {
            return reservationDao.getReservedRoomsPrice(reservation.getId());
        }

        @Override
        public void done() {
            try {
                displayInfo(reservation, get());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class LoadProperties extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            Properties defaultProperties = new Properties();
            defaultProperties.setProperty("localFee", "50");
            Properties properties = new Properties(defaultProperties);
            try {
                File configFile = new File(Constants.CONFIG_FILE_PATH);
                try (InputStream inputStream = new FileInputStream(configFile)) {
                    properties.load(inputStream);
                }
                return properties.getProperty("localFee");
            } catch (IOException ex) {
                return "50";
            }
        }

        @Override
        public void done() {
            try {
                localFee = Integer.parseInt(get());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}