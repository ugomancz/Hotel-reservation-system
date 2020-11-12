package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.GUI.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public final static Color backgroundColor = Color.lightGray;
    public final static int numberOfRooms = 15;
    public final static int week = 7;

    public static JFrame frame = new JFrame("HotelApp");
    public static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setContentPane(new MainPanel());
        frame.setVisible(true);
    }
}
