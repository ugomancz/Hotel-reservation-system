package cz.muni.fi.pv168.hotel.gui;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @author Ondrej Kostik
 */
public class BirthDatePicker {

    private final JPanel panel;
    private JComboBox<Integer> days, months, years;

    public BirthDatePicker() {
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(155, 25));
        initComboBoxes();
    }

    public JPanel getPanel() {
        return panel;
    }

    public LocalDate getDate() {
        int day = days.getSelectedIndex() + 1;
        int month = months.getSelectedIndex() + 1;
        int year = LocalDate.now().getYear();
        if (years.getSelectedItem() != null) {
            year = (Integer) years.getSelectedItem();
        }
        return LocalDate.of(year, month, day);
    }

    private void initComboBoxes() {
        days = new JComboBox<>();
        months = new JComboBox<>();
        years = new JComboBox<>();

        for (int month = 1; month <= 12; month++) {
            months.addItem(month);
        }
        months.addActionListener((e) -> updateDaysRange());
        months.setSelectedIndex(0);

        for (int year = LocalDate.now().getYear(); year >= 1900; year--) {
            years.addItem(year);
        }
        years.addActionListener((e) -> updateDaysRange());
        years.setSelectedIndex(0);

        panel.add(days);
        panel.add(months);
        panel.add(years);
    }

    private void updateDaysRange() {
        int month = months.getSelectedIndex() + 1;
        int year;
        if (years.getSelectedItem() == null) {
            year = LocalDate.now().getYear();
        } else {
            year = (Integer) years.getSelectedItem();
        }

        days.removeAllItems();
        for (int i = 1; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
            days.addItem(i);
        }
    }
}
