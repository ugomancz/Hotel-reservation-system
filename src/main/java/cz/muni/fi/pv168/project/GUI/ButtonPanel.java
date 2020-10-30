package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel {
    private final String[] buttonNames = {"New reservation","Change reservation","Cancel reservation",
            "Check-in","Check-out", "Room info", "Reservation info", "Log out", "Settings"};
    private final int numOfButtons = buttonNames.length;
    private ArrayList<Button> buttons = new ArrayList<>();

    public ButtonPanel() {
        super();
        this.setBackground(MainPanel.mainBackground);
        this.setLayout(new GridLayout(numOfButtons, 1, 0, 10));
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setPreferredSize(new Dimension(Button.dimension.width, 30));
        for (int i = 0; i < numOfButtons; i++) {
            buttons.add(new Button(buttonNames[i]));
            this.add(buttons.get(i));
        }
    }
}
