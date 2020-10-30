package cz.muni.fi.pv168.project;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HotelApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);
        frame.setContentPane(new MainPanel());
        frame.setVisible(true);
    }
}
