package cz.muni.fi.pv168.hotel.rooms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class RoomPicker {
    private static DefaultTableModel dataModel;
    private static JTable table;

    public static JTable createTable(){
        dataModel = new DefaultTableModel() {
            public int getColumnCount() { return 2; }

            private final String[] columns = {"check", "Room info"};
            @Override
            public String getColumnName(int column) {
                return columns[column];
            }
        };

        table = new JTable(dataModel);
        for (int i = 0; i < RoomDao.numberOfRooms();i++){
            table.add(new Checkbox(),RoomDao.ROOMS.get(i+1).toString());
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        return table;
    }
}
