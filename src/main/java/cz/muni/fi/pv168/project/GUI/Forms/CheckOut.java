package cz.muni.fi.pv168.project.GUI.Forms;

import javax.swing.*;

public class CheckOut extends Form {
    public CheckOut() {
        super("Check-out");
        this.setSize(250, 200);
        String receipt = "<html>Nights spent: %d<br/>" +
                "Room cost per night: %d<br/>" +
                "Local fees per person per night: %d<br/>" +
                "<u>Total: %d</u></html>";

        JLabel label = new JLabel(String.format(receipt, 3, 1200, 50, 5200), SwingConstants.CENTER);
        this.add(label);
    }
}