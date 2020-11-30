package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class CancelReservation extends JDialog {
    Button cancelButton, okayButton;
    JComboBox<String> reservationPicker;
    JCheckBox confirm;
    Map<String, Reservation> reservationMap = new HashMap<>();
    GridBagConstraints gbc = new GridBagConstraints();

    public CancelReservation() {
        super(MainWindow.frame, "Change Reservation", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setSize(300, 200);
        setLayout(new GridBagLayout());
        setupComboBox();
        initLayout();
        setVisible(true);
    }

    private void placeComponent(int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void setupComboBox() {
        for (Reservation reservation : Main.reservations) {
            reservationMap.put(reservation.getName(), reservation);
        }
        reservationPicker = new JComboBox<>();
        for (String name : reservationMap.keySet()) {
            reservationPicker.addItem(name);
        }
        reservationPicker.setSelectedIndex(0);
        reservationPicker.setPreferredSize(new Dimension(223, 20));
        reservationPicker.addActionListener(this::actionPerformed);
        reservationPicker.setFont(Button.font);
    }

    private void addButtons() {
        gbc.anchor = GridBagConstraints.SOUTH;
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        placeComponent(0, 10, okayButton);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        placeComponent(5, 10, cancelButton);
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;

        placeComponent(0, 0, new JLabel("Reservation: "));
        placeComponent(0, 5, new JLabel("Are u sure?"));

        gbc.anchor = GridBagConstraints.LINE_START;

        placeComponent(5, 0, reservationPicker);

        confirm = new JCheckBox();
        confirm.addActionListener(this::actionPerformed);
        placeComponent(5, 5, confirm);
        addButtons();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton)) {
            dispose();
        } else if (e.getSource().equals(okayButton)) {
            if (confirm.isSelected()) {
                JOptionPane.showMessageDialog(this, "tu sa niečo vykona");
            } else {
                JOptionPane.showMessageDialog(this, "Confirmation needed");
            }
        }
    }
}
