package cz.muni.fi.pv168.hotel.gui.forms;

import cz.muni.fi.pv168.hotel.gui.I18N;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GuestTable {

    private static final I18N I18N = new I18N(GuestTable.class);

    public static JTable createTable() {

        DefaultTableModel dataModel = new DefaultTableModel() {
            private final String[] columns = {I18N.getString("name"), I18N.getString("birthDate"), I18N.getString("IDnumber"), I18N.getString("roomNumber")};

            public int getColumnCount() {
                return 4;
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
        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        table.getColumnModel().getColumn(3).setPreferredWidth(15);
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }

}
