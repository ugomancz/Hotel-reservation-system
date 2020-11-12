package cz.muni.fi.pv168.project.gui.Forms;

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

        Main.frame.setEnabled(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onClose();
            }
        });
    }

    void onClose() {
        Main.frame.setEnabled(true);
        this.dispose();
    }
}
