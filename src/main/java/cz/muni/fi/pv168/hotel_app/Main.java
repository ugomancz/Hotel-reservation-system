package cz.muni.fi.pv168.hotel_app;

import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import java.util.ArrayList;

public class Main {

    public static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        new MainWindow();
    }
}
