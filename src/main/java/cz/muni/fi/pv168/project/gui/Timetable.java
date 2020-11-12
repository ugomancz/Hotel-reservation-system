package cz.muni.fi.pv168.project.gui;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.reservations.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;

import static cz.muni.fi.pv168.project.reservations.ReservationStatus.past;

public class Timetable extends JPanel {

    static JPanel[][] panels = new JPanel[Main.numberOfRooms][Main.week];
    public LocalDate currentDate = LocalDate.now();

    public Timetable() {
        super();
        this.setLayout(new GridLayout(Main.numberOfRooms, Main.week, 0, 0));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBackground(Main.backgroundColor);
        this.initPanels(Main.numberOfRooms, Main.week);
    }

    private void initPanels(int y, int x) {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(new LineBorder(Color.black, 1));
                panels[i][j] = panel;
                this.add(panel);
            }
        }
    }

    public void changeColor(Color color, int room) {
        for (int i = 1; i < 5; i++) {
            panels[room - 1][i].setBackground(color);
        }
    }

    public void changeName(String name, int room) {
        for (int i = 1; i < 5; i++) {
            if (panels[room - 1][i].getComponentCount() > 0)
                ((JLabel) panels[room - 1][i].getComponent(0)).setText(name);
            else {
                JLabel label = new JLabel(name, SwingConstants.CENTER);
                panels[room - 1][i].add(label);
            }
        }
        this.revalidate();
    }

    private boolean isInterfering(LocalDate newArrival, LocalDate newDeparture, LocalDate arrival, LocalDate departure) {
        return (newArrival.isAfter(arrival) && newArrival.isBefore(departure))
                || (newDeparture.isAfter(arrival) && newDeparture.isBefore(departure))
                || (newArrival.isBefore(arrival) && newDeparture.isAfter(departure));
    }

    public boolean isFree(int room, LocalDate arrival, LocalDate departure) {
        for (Reservation reservation : Main.reservations) {
            if (reservation.getStatus() != past && reservation.getRoomNumber() == room
                    && isInterfering(arrival, departure, reservation.getArrival(), reservation.getDeparture())) {
                return false;
            }
        }
        return true;
    }

    public void drawWeek() {

    }
}
