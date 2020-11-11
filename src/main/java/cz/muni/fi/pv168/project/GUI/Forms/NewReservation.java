package cz.muni.fi.pv168.project.GUI.Forms;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewReservation extends Form implements ActionListener {
    JButton button;
    public NewReservation() {
        super("New Reservation");
        this.setSize(new Dimension(400,400));
        //this.add(new DatePicker());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            this.dispose();
        }
    }
}
