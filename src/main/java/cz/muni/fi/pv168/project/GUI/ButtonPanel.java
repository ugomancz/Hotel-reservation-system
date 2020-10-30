package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ButtonPanel extends JPanel {
    private final ArrayList<String> buttonNames = new ArrayList<>(Arrays.asList("New reservation", "Change reservation", "Cancel reservation",
            "Check-in", "Check-out", "Room info", "Reservation info", "Log out", "Settings"));
    private final int numOfButtons = buttonNames.size();
    private final ArrayList<Button> buttons = new ArrayList<>();

    public ButtonPanel() {
        super();
        this.setLayout(new GridLayout(numOfButtons, 1, 5, 5));
        this.setBackground(MainPanel.mainBackground);
        this.setPreferredSize(new Dimension(Button.dimension.width, 500));

        for (int i = 0; i < numOfButtons; i++) {
            buttons.add(new Button(buttonNames.get(i)));
            this.add(buttons.get(i));
        }
    }
}
