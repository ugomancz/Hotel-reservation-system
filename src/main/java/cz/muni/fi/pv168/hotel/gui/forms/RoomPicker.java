package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RoomPicker {

    private static final I18N I18N = new I18N(RoomPicker.class);

    static JTable createTable(RoomDao roomDao) {
        DefaultTableModel dataModel = new DesignedTableModel(roomDao);

        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(0).setResizable(false);
        for (int i = 0; i < roomDao.numberOfRooms(); i++) {
            dataModel.addRow(new Object[0]);
            dataModel.setValueAt(false, i, 0);
            dataModel.setValueAt(roomDao.getRoom(i + 1).toString(), i, 1);
        }
        return table;
    }

    static class DesignedTableModel extends DefaultTableModel {

        private final String[] columns = {I18N.getString("checkColumn"), I18N.getString("descriptionColumn")};
        private final boolean[][] editable_cells;

        DesignedTableModel(RoomDao roomDao) {
            this.editable_cells = new boolean[roomDao.numberOfRooms()][columns.length];
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
