package cz.muni.fi.pv168.hotel_app.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.hotel_app.Constants;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SidePanel extends JPanel implements CalendarListener, DateHighlightPolicy {

    private static CalendarPanel calendar;
    final static Dimension dimension = new Dimension(220, 30);

    public SidePanel() {
        super();
        this.setBackground(Constants.BACKGROUND_COLOR);
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(dimension);

        this.add(new ButtonPanel(dimension), BorderLayout.CENTER);
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
            MainWindow.timetable.drawWeek(calendarSelectionEvent.getNewDate());
        } else {
            MainWindow.timetable.drawWeek(LocalDate.now());
        }
    }

    @Override
    public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent) {
    }

    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate localDate) {
        int reservations = MainWindow.timetable.getNumOfReservations(localDate);
        HighlightInformation highlight = new HighlightInformation();
        if (reservations == 0) {
            return null;
        } else if (reservations < Constants.NUMBER_OF_ROOMS *2/3) {
            highlight.colorBackground = Color.green;
        } else if (reservations < Constants.NUMBER_OF_ROOMS) {
            highlight.colorBackground = Color.orange;
        } else {
            highlight.colorBackground = new Color(250, 40, 40);
        }
        return highlight;
    }
}
