package cz.muni.fi.pv168.hotel.gui;

import com.github.lgooddatepicker.components.DatePicker;

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
        datePicker.getComponentToggleCalendarButton().setBackground(Button.background);
        datePicker.getComponentToggleCalendarButton().setFont(Button.font);
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
}
