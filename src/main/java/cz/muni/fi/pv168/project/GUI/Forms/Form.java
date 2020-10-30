package cz.muni.fi.pv168.project.GUI.Forms;

import cz.muni.fi.pv168.project.GUI.MainPanel;
import cz.muni.fi.pv168.project.Main;

import javax.swing.*;

public abstract class Form extends JFrame {
    public Form(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(Main.frame);
        this.setVisible(true);
    }
}
