package cz.muni.fi.pv168.project.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SidePanel extends JPanel implements CalendarListener, DateHighlightPolicy {
    private static CalendarPanel calendar;
    public SidePanel() {
        super();
        this.setBackground(Main.BACKGROUND_COLOR);
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(new Dimension(Button.dimension.width, 500));

        this.add(new ButtonPanel(), BorderLayout.CENTER);
        this.add(initCalendar(), BorderLayout.SOUTH);
    }

    public static CalendarPanel getCalendar() {
        return calendar;
    }

    private CalendarPanel initCalendar() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        settings.setVisibleNextYearButton(false);
        settings.setVisiblePreviousYearButton(false);
        settings.setHighlightPolicy(this);
        settings.setVisibleClearButton(false);
        calendar = new CalendarPanel(settings);
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

    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate localDate) {
        int reservations = MainPanel.timetable.getNumOfReservations(localDate);
        HighlightInformation highlight = new HighlightInformation();
        if (reservations == 0) {
            return null;
        } else if (reservations < Main.NUMBER_OF_ROOMS *2/3) {
            highlight.colorBackground = Color.green;
        } else if (reservations < Main.NUMBER_OF_ROOMS) {
            highlight.colorBackground = Color.orange;
        } else {
            highlight.colorBackground = new Color(250, 40, 40);
        }
        return highlight;
    }
}
