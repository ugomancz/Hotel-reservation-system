package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarPanel extends JPanel {
    public CalendarPanel() {
        super(new BorderLayout(10, 10));
        this.setBackground(Color.darkGray);
        this.setPreferredSize(new Dimension(100, 250));

        /*String monthString = new SimpleDateFormat("MMM")
                .format(Calendar.getInstance().getTime());
        JLabel monthLabel = new JLabel("CALENDAR");
        monthLabel.setForeground(Color.cyan);

        Button previousMonth = new Button("<");
        Button nextMonth = new Button(">");

        this.add(monthLabel, BorderLayout.CENTER);
        this.add(previousMonth, BorderLayout.WEST);
        this.add(nextMonth, BorderLayout.EAST);
        this.add(new JLabel(), BorderLayout.NORTH);
        this.add(new JLabel(), BorderLayout.SOUTH);*/

    }
}
