package cz.muni.fi.pv168.hotel_app.gui.forms;

import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.rooms.RoomDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoomInfo extends JDialog {
    JTextArea area = new JTextArea();

    public RoomInfo() {
        super(MainWindow.frame, "Room info", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setMinimumSize(new Dimension(400,360));
        area.setEditable(false);
        area.setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(240, 240, 240));

        for (int i = 0; i < 15; i++) {
            area.append(RoomDao.getRoom(i + 1).toString() + "\n");
        }
        add(area);
        pack();
        setVisible(true);
    }
}
