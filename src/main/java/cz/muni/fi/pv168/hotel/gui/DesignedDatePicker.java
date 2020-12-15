package cz.muni.fi.pv168.hotel.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Ondrej Kostik
 */
public class DesignedDatePicker {

    private final DatePicker datePicker;

    public DesignedDatePicker() {
        datePicker = new DatePicker();
        datePicker.getSettings().setFirstDayOfWeek(DayOfWeek.MONDAY);
        datePicker.getComponentToggleCalendarButton().setBackground(Button.BACKGROUND);
        datePicker.getComponentToggleCalendarButton().setFont(Button.FONT);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public LocalDate getDate() {
        return datePicker.getDate();
    }

    public void setDate(LocalDate date) {
        datePicker.setDate(date);
    }

    public void addDateChangeListener(DateChangeListener listener) {
        datePicker.addDateChangeListener(listener);
    }

    public void setFirstAllowedDate(LocalDate date) {
        datePicker.getSettings().setDateRangeLimits(date, date.plusYears(200));
    }
}
