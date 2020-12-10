package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.data.ReservationDao;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class SidePanel extends JPanel {

    final static Dimension dimension = new Dimension(255, 30);

    public SidePanel(ReservationDao reservationDao) {
        super();
        setBackground(Constants.BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(dimension);

        add(new ButtonPanel(dimension, reservationDao).getPanel(), BorderLayout.CENTER);
        add(new DesignedCalendar(reservationDao).getCalendar(), BorderLayout.SOUTH);
    }
}
