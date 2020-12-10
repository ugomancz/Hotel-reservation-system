package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;

public class RoomInfo extends JDialog {

    JTextArea area = new JTextArea();

    public RoomInfo(JFrame frame) {
        super(frame, "Room info", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        setMinimumSize(new Dimension(400, 360));
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
