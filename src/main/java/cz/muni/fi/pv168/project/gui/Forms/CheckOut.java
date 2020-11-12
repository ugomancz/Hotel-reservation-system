package cz.muni.fi.pv168.project.gui.Forms;

import cz.muni.fi.pv168.project.gui.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckOut extends Form implements ActionListener {
    Dimension dimension = new Dimension(250, 200);
    JButton closeButton;
    String receipt = "<html>Client: %s<br/><br/>" +
            "Nights spent: %d<br/>" +
            "Room cost per night: %d<br/>" +
            "Local fees per person per night: %d<br/><br/>" +
            "<u>Total: %d</u></html>";

    public CheckOut() {
        super("Check-out");
        this.setSize(dimension);
        this.setResizable(false);
        this.setLayout(new BorderLayout(0, 0));

        JLabel label = new JLabel(String.format(receipt, "Petr Novak", 3, 1200, 50, 5200), SwingConstants.CENTER);
        this.add(label, BorderLayout.CENTER);

        closeButton = new Button("Close");
        closeButton.setMaximumSize(new Dimension(100, 30));
        closeButton.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(closeButton);

        this.add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            onClose();
        }
    }
}