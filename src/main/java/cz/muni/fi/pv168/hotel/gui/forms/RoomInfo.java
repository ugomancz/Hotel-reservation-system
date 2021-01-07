package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author Timotej Cirok
 */
public class RoomInfo {

    private static final I18N I18N = new I18N(RoomInfo.class);
    private final RoomDao roomDao;
    private final JTextArea textArea;

    public RoomInfo(JFrame frame, RoomDao roomDao) {
        this.roomDao = roomDao;
        JDialog dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setMinimumSize(new Dimension(400, 360));
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.setBackground(new Color(240, 240, 240));
        new LoadRooms().execute();
        dialog.add(textArea);
        dialog.pack();
        dialog.setVisible(true);
    }

    private class LoadRooms extends SwingWorker<List<Room>, Void> {
        @Override
        protected List<Room> doInBackground() {
            return roomDao.findAll();
        }

        @Override
        protected void done() {
            try {
                for (Room room : get()) {
                    textArea.append(room.toString() + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
