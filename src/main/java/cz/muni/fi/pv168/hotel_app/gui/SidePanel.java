package cz.muni.fi.pv168.hotel_app.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.hotel_app.Constants;
import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.rooms.RoomDao;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SidePanel extends JPanel implements CalendarListener {

    final static Dimension dimension = new Dimension(255, 30);
    private final ReservationDao reservationDao;

    public SidePanel(ReservationDao reservationDao) {
        super();
        setBackground(Constants.BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(dimension);
        this.reservationDao = reservationDao;

        add(new ButtonPanel(dimension, reservationDao), BorderLayout.CENTER);
        add(initCalendar(), BorderLayout.SOUTH);
    }

    private CalendarPanel initCalendar() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        settings.setVisibleNextYearButton(false);
        settings.setVisiblePreviousYearButton(false);
        settings.setHighlightPolicy(this::getHighlightInformationOrNull);
        settings.setVisibleClearButton(false);
        CalendarPanel calendar = new CalendarPanel(settings);
        calendar.setBackground(Button.background);
        calendar.addCalendarListener(this);
        return calendar;
    }

    @Override
    public void selectedDateChanged(CalendarSelectionEvent calendarSelectionEvent) {
        if (calendarSelectionEvent.getNewDate() != null) {
            Timetable.drawWeek(calendarSelectionEvent.getNewDate());
            MainWindow.DayNames.changeDates(calendarSelectionEvent.getNewDate());
        } else {
            Timetable.drawWeek(LocalDate.now());
            MainWindow.DayNames.changeDates(LocalDate.now());
        }
    }

    @Override
    public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent) {
    }

    public HighlightInformation getHighlightInformationOrNull(LocalDate localDate) {
        int reservations = reservationDao.getNumOfReservations(localDate);
        if (reservations == 0) {
            return null;
        }
        HighlightInformation highlight = new HighlightInformation(Color.white);
        if (reservations == RoomDao.numberOfRooms()) {
            highlight.colorBackground = new Color(250, 40, 40);
        } else if (reservations >= RoomDao.numberOfRooms() * 2 / 3) {
            highlight.colorBackground = Color.orange;
        }
        return highlight;
    }
}
