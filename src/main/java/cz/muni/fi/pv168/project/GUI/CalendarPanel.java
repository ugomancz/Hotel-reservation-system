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

        this.add(new JLabel(new SimpleDateFormat("MMM")
                        .format(Calendar.getInstance().getTime())), BorderLayout.CENTER);

    }
}
