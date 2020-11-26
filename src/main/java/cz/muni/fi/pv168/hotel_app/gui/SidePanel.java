package cz.muni.fi.pv168.hotel_app.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.hotel_app.Constants;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SidePanel extends JPanel implements CalendarListener {

    final static Dimension dimension = new Dimension(220, 30);
    private static CalendarPanel calendar;

    public SidePanel() {
        super();
        setBackground(Constants.BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(dimension);

        add(new ButtonPanel(dimension), BorderLayout.CENTER);
        add(initCalendar(), BorderLayout.SOUTH);
    }

    public static CalendarPanel getCalendar() {
        return calendar;
    }

    private CalendarPanel initCalendar() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        settings.setVisibleNextYearButton(false);
        settings.setVisiblePreviousYearButton(false);
        settings.setHighlightPolicy(this::getHighlightInformationOrNull);
        settings.setVisibleClearButton(false);
        calendar = new CalendarPanel(settings);
        calendar.setBackground(new Color(240, 240, 240));
        calendar.addCalendarListener(this);
        return calendar;
    }

    @Override
    public void selectedDateChanged(CalendarSelectionEvent calendarSelectionEvent) {
        if (calendarSelectionEvent.getNewDate() != null) {
            MainWindow.timetable.drawWeek(calendarSelectionEvent.getNewDate());
            MainWindow.DayNames.changeDates(calendarSelectionEvent.getNewDate());
        } else {
            MainWindow.timetable.drawWeek(LocalDate.now());
            MainWindow.DayNames.changeDates(LocalDate.now());
        }
    }

    @Override
    public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent) {
    }

    public HighlightInformation getHighlightInformationOrNull(LocalDate localDate) {
        int reservations = MainWindow.timetable.getNumOfReservations(localDate);
        HighlightInformation highlight = new HighlightInformation();
        if (reservations == 0) {
            return null;
        } else if (reservations == Constants.NUMBER_OF_ROOMS) {
            highlight.colorBackground = new Color(250, 40, 40);
        } else if (reservations > Constants.NUMBER_OF_ROOMS * 2 / 3) {
            highlight.colorBackground = Color.orange;
        }
        return highlight;
    }
}
