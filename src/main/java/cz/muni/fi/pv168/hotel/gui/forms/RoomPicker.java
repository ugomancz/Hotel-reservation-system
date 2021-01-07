package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RoomPicker {

    private static final I18N I18N = new I18N(RoomPicker.class);
    private static List<Room> rooms;

    static JTable createTable(RoomDao roomDao) {
        rooms = roomDao.findAll();
        DefaultTableModel dataModel = new DesignedTableModel();
        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(0).setResizable(false);
        for (int i = 0; i < rooms.size(); i++) {
            dataModel.addRow(new Object[0]);
            dataModel.setValueAt(false, i, 0);
            dataModel.setValueAt(rooms.get(i).toString(), i, 1);
        }
        return table;
    }

    static class DesignedTableModel extends DefaultTableModel {

        private final String[] columns = {I18N.getString("checkColumn"), I18N.getString("descriptionColumn")};
        private final boolean[][] editable_cells;

        DesignedTableModel() {
            this.editable_cells = new boolean[rooms.size()][columns.length];
        }

        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return editable_cells[row][column];
        }

        void setCellEditable(int row, int column, boolean value) {
            editable_cells[row][column] = value;
            fireTableCellUpdated(row, column);
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
    }
}
