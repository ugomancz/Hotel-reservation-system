package cz.muni.fi.pv168.hotel.gui;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
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
    private final RoomDao roomDao;

    DesignedCalendar(ReservationDao reservationDao, RoomDao roomDao) {
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        CALENDAR.setSettings(initSettings());
        CALENDAR.addCalendarListener(new CalendarEvent());
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
        } else if (reservations == roomDao.numberOfRooms()) {
            highlight.colorBackground = new Color(250, 40, 40);
        } else if (reservations >= roomDao.numberOfRooms() * 2 / 3) {
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
