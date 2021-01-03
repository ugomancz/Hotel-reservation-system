package cz.muni.fi.pv168.hotel.gui.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GuestTable {

    public static JTable createTable(String name, String birthDate, String id) {
        //all cells false
        DefaultTableModel dataModel = new DefaultTableModel() {
            public int getColumnCount() {
                return 3;
            }

            private final String[] columns = {name, birthDate, id};

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable table = new JTable(dataModel);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }

}
