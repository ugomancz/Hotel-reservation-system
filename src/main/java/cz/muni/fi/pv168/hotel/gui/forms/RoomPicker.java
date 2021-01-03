package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RoomPicker {

    private static DefaultTableModel dataModel;
    private static JTable table;

    public static JTable createTable(RoomDao roomDao) {
        dataModel = new DefaultTableModel() {
            private final String[] columns = {"check", "Room info"};

            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        table = new JTable(dataModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        for (int i = 0; i < roomDao.numberOfRooms(); i++) {
            dataModel.addRow(new Object[0]);
            dataModel.setValueAt(false, i, 0);
            dataModel.setValueAt(roomDao.getRoom(i + 1).toString(), i, 1);

        }
        return table;
    }
}
