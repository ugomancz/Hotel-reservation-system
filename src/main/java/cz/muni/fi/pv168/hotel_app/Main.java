package cz.muni.fi.pv168.hotel_app;

import cz.muni.fi.pv168.hotel_app.gui.MainPanel;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static ArrayList<Reservation> reservations = new ArrayList<>();

    public static JFrame frame = new JFrame("HotelApp");

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setContentPane(new MainPanel());
        frame.setVisible(true);
    }
}
