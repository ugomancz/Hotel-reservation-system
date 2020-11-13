package cz.muni.fi.pv168.project.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SidePanel extends JPanel implements CalendarListener {
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
        calendar.addCalendarListener(this);
        return calendar;
    }

    @Override
    public void selectedDateChanged(CalendarSelectionEvent calendarSelectionEvent) {
        if (calendarSelectionEvent.getNewDate() != null) {
            MainPanel.timetable.drawWeek(calendarSelectionEvent.getNewDate());
        } else {
            MainPanel.timetable.drawWeek(LocalDate.now());
        }
    }

    @Override
    public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent) {
    }
}
