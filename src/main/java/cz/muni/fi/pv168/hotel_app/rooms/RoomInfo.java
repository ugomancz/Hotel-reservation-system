package cz.muni.fi.pv168.hotel_app.rooms;

import cz.muni.fi.pv168.hotel_app.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class RoomInfo extends JDialog{
    JTextPane pane = new JTextPane();
    GridBagConstraints gbc = new GridBagConstraints();

    public RoomInfo() {
        super(MainWindow.frame, "Room info", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(MainWindow.frame);
        setSize(400, 400);
        setEnabled(true);
        setLayout(new GridBagLayout());

        add(pane,gbc);
        for (int i = 0; i < 15;i++){
            pane.setText(RoomDao.getRoom(i).toString());
        }
        setVisible(true);
    }
}
