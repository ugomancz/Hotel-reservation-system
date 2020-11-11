package cz.muni.fi.pv168.project.GUI;

import com.github.lgooddatepicker.components.CalendarPanel;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;

public class SidePanel extends JPanel {
    public SidePanel() {
        super();
        this.setBackground(Main.backgroundColor);
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(new Dimension(Button.dimension.width, 500));

        this.add(new ButtonPanel(), BorderLayout.CENTER);
        this.add(initCalendar(), BorderLayout.SOUTH);
    }

    private CalendarPanel initCalendar() {
        CalendarPanel calendar = new CalendarPanel();
        calendar.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        calendar.setBackground(new Color(240, 240, 240));
        return calendar;
    }
}
