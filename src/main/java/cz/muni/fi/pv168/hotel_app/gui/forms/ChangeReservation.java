package cz.muni.fi.pv168.hotel_app.gui.forms;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.hotel_app.gui.Button;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;

/**
 * @author Ondrej Kostik
 */
public class ChangeReservation extends JDialog {

    Button cancelButton, okayButton;
    JTextField name, phone, email, people;
    JComboBox<Integer> roomPicker;
    DatePicker arrival, departure;
    Integer[] roomNumbers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    GridBagConstraints gbc = new GridBagConstraints();

    public ChangeReservation() {
        super(MainWindow.frame, "Change Reservation", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(MainWindow.frame);
        setLayout(new GridBagLayout());
        initLayout();
        setSize(400, 400);
        setResizable(false);
        setVisible(true);
    }

    private void initLayout() {
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(new JLabel("Name and surname: "), gbc, 0, 0);
        addComponent(new JLabel("Phone: "), gbc, 0, 10);
        addComponent(new JLabel("Email: "), gbc, 0, 20);
        addComponent(new JLabel("Number of people: "), gbc, 0, 30);
        addComponent(new JLabel("From: "), gbc, 0, 40);
        addComponent(new JLabel("To: "), gbc, 0, 50);
        addComponent(new JLabel("Room number: "), gbc, 0, 60);
        addButtons();

        gbc.anchor = GridBagConstraints.LINE_START;
        addTextFields();
        addDatePickers();
        addComboBox();
    }

    private void addComponent(JComponent component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void addTextFields() {
        name = new JTextField(13);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        name.setEditable(true);
        addComponent(name, gbc, 5, 0);

        phone = new JTextField(13);
        phone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        phone.setEditable(true);
        addComponent(phone, gbc, 5, 10);

        email = new JTextField(13);
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        email.setEditable(true);
        addComponent(email, gbc, 5, 20);

        people = new JTextField(13);
        people.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        people.setEditable(true);
        addComponent(people, gbc, 5, 30);
    }

    private void addDatePickers() {
        arrival = new DatePicker();
        arrival.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        arrival.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        arrival.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(arrival, gbc, 5, 40);

        departure = new DatePicker();
        departure.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        departure.getComponentToggleCalendarButton().setBackground(new Color(240, 240, 240));
        departure.getComponentToggleCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(departure, gbc, 5, 50);
    }

    private void addButtons() {
        okayButton = new Button("OK");
        okayButton.addActionListener(this::actionPerformed);
        addComponent(okayButton, gbc, 0, 70);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this::actionPerformed);
        addComponent(cancelButton, gbc, 5, 70);
    }

    private void addComboBox() {
        roomPicker = new JComboBox<>(roomNumbers);
        roomPicker.setSelectedIndex(0);
        roomPicker.addActionListener(this::actionPerformed);
        roomPicker.setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(roomPicker, gbc,5,60);
    }

    private void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource().toString());
    }
}
