package cz.muni.fi.pv168.hotel;

import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.gui.MainWindow;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        ReservationDao reservationDao = new ReservationDao(createDataSource());
        EventQueue.invokeLater(() -> MainWindow.run(reservationDao));
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/hotel-app";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
}
