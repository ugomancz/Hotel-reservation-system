package cz.muni.fi.pv168.project.gui.Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.muni.fi.pv168.project.gui.Button;
import cz.muni.fi.pv168.project.gui.MainPanel;

public class CheckIn extends Form implements ActionListener {

    GridBagConstraints gbc = new GridBagConstraints();
    String room;
    String name;


    public CheckIn() {
        super("Check-in");
        this.setSize(new Dimension(500, 400));

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        gbc.weightx = 0;
        gbc.weighty = 1;






        JLabel IDnumber = new JLabel("ID number ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(IDnumber, gbc);
        JTextField IDfield = new JTextField(16);
        IDfield.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(IDfield);
        Button findReservation = new Button("Find reservation");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(findReservation);


        JLabel nameLabel = new JLabel("Name ");
        gbc.gridx =0;
        gbc.gridy =1;
        //gbc.insets = new Insets(0, 0, 0, 0);
        this.add(nameLabel, gbc);
        JTextField nameField = new JTextField(16);
        nameField.setEditable(true);
        gbc.gridx =1;
        gbc.gridy =1;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(nameField, gbc);


        JLabel phoneLabel = new JLabel("Phone number ");
        gbc.gridx =0;
        gbc.gridy =2;
        this.add(phoneLabel, gbc);
        JTextField phoneField = new JTextField(16);
        phoneField.setEditable(true);
        gbc.gridx =1;
        gbc.gridy =2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(phoneField, gbc);

        JLabel roomLabel = new JLabel("Room number ");
        gbc.gridx =0;
        gbc.gridy =3;
        this.add(roomLabel, gbc);
        JTextField roomField = new JTextField(4);
        roomField.setEditable(true);
        gbc.gridx =1;
        gbc.gridy =3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(roomField, gbc);


        JLabel lengthLabel = new JLabel("Length of stay ");
        gbc.gridx =0;
        gbc.gridy =4;
        this.add(lengthLabel, gbc);
        JTextField lengthField = new JTextField(4);
        roomField.setEditable(true);
        gbc.gridx =1;
        gbc.gridy =4;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(lengthField, gbc);
        JLabel days = new JLabel(" days");
        gbc.gridx = 2;
        gbc.gridy = 4;
        this.add(days, gbc);



        Button confirm, cancel, change;
        confirm = new Button("Confirm");
        cancel = new Button("Cancel");
        //change = new Button("Change Reservation");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        this.add(cancel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        this.add(confirm, gbc);

        cancel.addActionListener(this);
        confirm.addActionListener(this);

        this.name = nameField.getText();
        this.room = roomField.getText();



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Cancel")) {
            //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            onClose();

        } else if (action.equals("Confirm")) {
            //MainPanel.timetable.setPanelColor(Color.red, 5);
            //MainPanel.timetable.changeName("Timotej Cirok", 5);
            onClose();
        }


    }
}
