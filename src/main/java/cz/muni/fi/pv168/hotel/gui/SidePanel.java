package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.data.ReservationDao;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class SidePanel {

    final static Dimension dimension = new Dimension(255, 30);
    private final JPanel panel;

    public SidePanel(ReservationDao reservationDao) {
        panel = new JPanel();
        panel.setBackground(Constants.BACKGROUND_COLOR);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setPreferredSize(dimension);

        panel.add(new ButtonPanel(reservationDao).getPanel(), BorderLayout.CENTER);
        panel.add(new DesignedCalendar(reservationDao).getCalendar(), BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
