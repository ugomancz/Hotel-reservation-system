package cz.muni.fi.pv168.project.GUI.Forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.project.GUI.Button;
import cz.muni.fi.pv168.project.GUI.Timetable;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class NewReservation extends Form implements ActionListener {
    cz.muni.fi.pv168.project.GUI.Button cancelled, okay;
    JTextField name, phone, email, people;

    GridBagConstraints gbc = new GridBagConstraints();
    public NewReservation() {
        super("New Reservation");
        this.setSize(new Dimension(400,400));
        this.setLayout(new GridBagLayout());

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Name and surname: "),gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(new JLabel("Phone: "),gbc);

        gbc.gridx = 0;
        gbc.gridy = 20;
        add(new JLabel("email: "),gbc);

        gbc.gridx = 0;
        gbc.gridy = 30;
        this.add(new JLabel("Number of people: "),gbc);

        gbc.gridx = 0;
        gbc.gridy = 40;
        add(new JLabel("from: "),gbc);

        gbc.gridx = 0;
        gbc.gridy = 50;
        add(new JLabel("to: "),gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        name = new JTextField(13);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        name.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 0;
        add(name,gbc);

        phone = new JTextField(13);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        phone.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 10;
        add(phone,gbc);

        email = new JTextField(13);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        email.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 20;
        add(email,gbc);

        people = new JTextField(13);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        people.setEditable(true);
        gbc.gridx = 5;
        gbc.gridy = 30;
        add(people,gbc);

        gbc.gridx = 5;
        gbc.gridy = 40;
        add(new DatePicker(),gbc);

        gbc.gridx = 5;
        gbc.gridy = 50;
        add(new DatePicker(),gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        okay = new Button("OK");
        okay.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 60;
        add(okay,gbc);

        cancelled = new cz.muni.fi.pv168.project.GUI.Button("Cancel");
        cancelled.addActionListener(this);
        gbc.gridx = 5;
        gbc.gridy = 60;
        add(cancelled,gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelled) {
            Main.frame.setEnabled(true);
            this.dispose();
        }
    }
}
