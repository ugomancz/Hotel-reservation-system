package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;

/**
 * @author Timotej Cirok
 */
public class RoomInfo {

    private static final I18N I18N = new I18N(RoomInfo.class);

    public RoomInfo(JFrame frame, RoomDao roomDao) {
        JDialog dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(400, 360));
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.setBackground(new Color(240, 240, 240));

        for (int i = 0; i < 15; i++) {
            area.append(roomDao.getRoom(i + 1).toString() + "\n");
        }
        dialog.add(area);
        dialog.pack();
        dialog.setVisible(true);
    }
}
