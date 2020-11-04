package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.GUI.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame frame = new JFrame("HotelApp");
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setMinimumSize(new Dimension(640, 400));
        frame.setContentPane(new MainPanel());
        frame.setVisible(true);
    }
}
