package cz.muni.fi.pv168.hotel.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import java.awt.Color;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Ondrej Kostik
 */
class DesignedCalendar {

    private static final CalendarPanel CALENDAR = new CalendarPanel();
    private final ReservationDao reservationDao;

    DesignedCalendar(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        CALENDAR.setSettings(initSettings());
        CALENDAR.addCalendarListener(new CalendarEvent());
        CALENDAR.getPreviousMonthButton().setBackground(Constants.BUTTON_BACKGROUND);
        CALENDAR.getPreviousMonthButton().setFont(Constants.BUTTON_FONT);
        CALENDAR.getNextMonthButton().setBackground(Constants.BUTTON_BACKGROUND);
        CALENDAR.getNextMonthButton().setFont(Constants.BUTTON_FONT);
    }

    static void setDate(LocalDate date) {
        CALENDAR.setSelectedDate(date);
    }

    CalendarPanel getCalendar() {
        return CALENDAR;
    }

    private DatePickerSettings initSettings() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        settings.setVisibleNextYearButton(false);
        settings.setVisiblePreviousYearButton(false);
        settings.setHighlightPolicy(this::getHighlightInformation);
        settings.setVisibleClearButton(false);
        return settings;
    }

    private HighlightInformation getHighlightInformation(LocalDate localDate) {
        int reservations = reservationDao.getNumOfReservations(localDate);
        HighlightInformation highlight = new HighlightInformation(Color.WHITE);
        if (localDate.equals(LocalDate.now())) {
            highlight.colorBackground = new Color(110, 220, 250);
        } else if (reservations == RoomDao.numberOfRooms()) {
            highlight.colorBackground = new Color(250, 40, 40);
        } else if (reservations >= RoomDao.numberOfRooms() * 2 / 3) {
            highlight.colorBackground = Color.ORANGE;
        }
        return highlight;
    }

    private static class CalendarEvent implements CalendarListener {

        @Override
        public void selectedDateChanged(CalendarSelectionEvent calendarSelectionEvent) {
            if (calendarSelectionEvent.getNewDate() != null) {
                Timetable.drawWeek(calendarSelectionEvent.getNewDate());
                TimetableHeader.changeDates(calendarSelectionEvent.getNewDate());
            } else {
                Timetable.drawWeek(LocalDate.now());
                TimetableHeader.changeDates(LocalDate.now());
            }
        }

        @Override
        public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent) {
        }
    }
}
