package cz.muni.fi.pv168.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import cz.muni.fi.pv168.project.Button;

public class MainPanel extends JPanel {
    final static Color mainBackground = Color.lightGray;
    final static int numOfButtons = 12;
    public MainPanel() {
        super(new BorderLayout(10,10));
        this.setBackground(mainBackground);
        this.setBorder(new EmptyBorder(10,10,10,10));

        /* timetable panel creation */
        JPanel timetable = new JPanel();
        timetable.setBackground(Color.white);

        /* calendar setting panel creation */
        JPanel calendar = new JPanel();

        /* button panel creation */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(mainBackground);
        buttonPanel.setLayout(new GridLayout(numOfButtons,1, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(Button.dimension.width,100));

        buttonPanel.add(new Button("Nová rezervace"));
        buttonPanel.add(new Button("Změnit rezervaci"));
        buttonPanel.add(new Button("Zrušit rezervaci"));
        buttonPanel.add(new Button("Check-in"));
        buttonPanel.add(new Button("Check-out"));

        this.add(buttonPanel, BorderLayout.EAST);
        this.add(timetable, BorderLayout.CENTER);
    }
}
