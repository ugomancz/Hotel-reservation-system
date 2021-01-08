package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
    private final JDialog dialog;

    public RoomInfo(JFrame frame, RoomDao roomDao) {
        this.roomDao = roomDao;
        dialog = new JDialog(frame, I18N.getString("windowTitle"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.getRootPane().registerKeyboardAction((e) -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        initLayout();
        dialog.setVisible(true);
    }

    private void initLayout() {
        JTable table = createTable();
        dialog.getRootPane().setBorder(new EmptyBorder(1, 1, 1, 1));
        dialog.setBackground(new Color(240, 240, 240));
        new LoadRooms((DefaultTableModel) table.getModel()).execute();
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(450, 247));
        dialog.add(pane);
    }

    private JTable createTable() {
        DefaultTableModel dataModel = new DefaultTableModel() {
            private final String[] columns = {I18N.getString("roomNumberColumn"), I18N.getString("singleBedsColumn"),
                    I18N.getString("kingSizeBedsColumn"), I18N.getString("roomPriceColumn")};

            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(0).setMaxWidth(55);
        table.getColumnModel().getColumn(0).setResizable(false);
        return table;
    }


    private class LoadRooms extends SwingWorker<List<Room>, Void> {

        private final DefaultTableModel model;

        public LoadRooms(DefaultTableModel model) {
            this.model = model;
        }

        @Override
        protected List<Room> doInBackground() {
            return roomDao.findAll();
        }

        @Override
        protected void done() {
            try {
                for (Room room : get()) {
                    model.addRow(new Object[]{room.getRoomNumber(), room.getStandardBeds(), room.getKingsizeBeds(), room.getPricePerNight()});
                }
                dialog.pack();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
