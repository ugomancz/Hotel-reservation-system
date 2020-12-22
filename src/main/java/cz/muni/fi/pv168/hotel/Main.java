package cz.muni.fi.pv168.hotel;

import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.gui.MainWindow;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        ReservationDao reservationDao = new ReservationDao(dataSource);
        RoomDao roomDao = null;
        GuestDao guestDao = new GuestDao(dataSource);
        EventQueue.invokeLater(() -> MainWindow.run(reservationDao, guestDao, roomDao));
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/hotel-app";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
}
