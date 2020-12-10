package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author Ondrej Kostik
 */
class TimetableHeader {

    private static final JLabel[] LABELS = new JLabel[Constants.DAYS_IN_WEEK];
    private static LocalDate current;
    private final JPanel panel;
    private Button previous, next;

    TimetableHeader(LocalDate date) {
        panel = new JPanel();
        current = date;
        panel.setBorder(new EmptyBorder(0, 75, 0, 0));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        panel.setLayout(new BorderLayout(5, 0));

        initDayNames();
        changeDates(current);
    }

    static void changeDates(LocalDate date) {
        LocalDate day = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        current = day;
        for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
            String labelText = DateTimeFormatter.ofPattern("E dd.MM.").format(day); // e.g. Mon 23.11.
            if (day.isEqual(LocalDate.now())) { // current date gets highlighted
                LABELS[i].setText(String.format("<html><u>%s</u></html>", labelText));
            } else {
                LABELS[i].setText(labelText);
            }
            day = day.plusDays(1);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void initLabels(JPanel panel) {
        for (int i = 0; i < Constants.DAYS_IN_WEEK; i++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            LABELS[i] = label;
            panel.add(label);
        }
    }

    private void initButtons(JPanel panel) {
        previous = new Button("<<");
        previous.addActionListener(this::actionPerformed);
        panel.add(previous);

        next = new Button(">>");
        next.addActionListener(this::actionPerformed);
        panel.add(next);
    }

    private void initDayNames() {
        JPanel dayNames = new JPanel();
        dayNames.setBackground(Constants.BACKGROUND_COLOR);
        dayNames.setLayout(new GridLayout(1, Constants.DAYS_IN_WEEK));
        initLabels(dayNames);
        panel.add(dayNames, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setBackground(Constants.BACKGROUND_COLOR);
        buttons.setPreferredSize(new Dimension(SidePanel.dimension.width, 20));
        buttons.setLayout(new GridLayout(1, 2, 5, 0));
        initButtons(buttons);
        panel.add(buttons, BorderLayout.EAST);
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(previous)) {
            current = current.minusWeeks(1);
            changeDates(current);
            Timetable.drawWeek(current);
            DesignedCalendar.setDate(current);
        } else if (e.getSource().equals(next)) {
            current = current.plusWeeks(1);
            changeDates(current);
            Timetable.drawWeek(current);
            DesignedCalendar.setDate(current);
        }
    }
}
