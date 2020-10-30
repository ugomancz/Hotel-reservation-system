package cz.muni.fi.pv168.project.GUI.Forms;

import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class Form extends JFrame {
    public Form(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(Main.frame);
        this.setVisible(true);

        Main.frame.setVisible(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onClose();
            }
        });
    }

    private void onClose() {
        Main.frame.setVisible(true);
        this.dispose();
    }
}
