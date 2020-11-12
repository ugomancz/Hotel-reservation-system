package cz.muni.fi.pv168.project.gui;

import cz.muni.fi.pv168.project.gui.Forms.CheckIn;
import cz.muni.fi.pv168.project.gui.Forms.CheckOut;
import cz.muni.fi.pv168.project.gui.Forms.NewReservation;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class ButtonPanel extends JPanel implements ActionListener {
    private static final ArrayList<String> buttonNames = new ArrayList<>(Arrays.asList("New reservation", "Change reservation", "Cancel reservation",
            "Check-in", "Check-out", "Room info", "Reservation info", "Settings"));
    private final ArrayList<Button> buttons = new ArrayList<>();

    public ButtonPanel() {
        super();
        int numOfButtons = buttonNames.size();
        this.setLayout(new GridLayout(numOfButtons, 1, 5, 5));
        this.setBackground(Main.backgroundColor);
        this.setPreferredSize(new Dimension(Button.dimension.width, 500));

        for (int i = 0; i < numOfButtons; i++) {
            buttons.add(new Button(buttonNames.get(i)));
            buttons.get(i).addActionListener(this);
            this.add(buttons.get(i));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (buttons.get(0) == (e.getSource())) {
            new NewReservation();
        } else if (buttons.get(1) == e.getSource()) {
            notImplemented();
            //new ChangeReservation();
        } else if (buttons.get(2) == e.getSource()) {
            notImplemented();
            //new CancelReservation();
        } else if (buttons.get(3) == e.getSource()) {
            new CheckIn();
        } else if (buttons.get(4) == e.getSource()) {
            new CheckOut();
        } else if (buttons.get(5) == e.getSource()) {
            notImplemented();
            //new RoomInfo();
        } else if (buttons.get(6) == e.getSource()) {
            notImplemented();
            //new ReservationInfo();
        } else if (buttons.get(7) == e.getSource()) {
            notImplemented();
            //new Settings();
        }
    }

    private void notImplemented() {
        JOptionPane.showInternalMessageDialog(null, "Not yet implemented");
    }
}
