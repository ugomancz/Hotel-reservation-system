package cz.muni.fi.pv168.hotel_app;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        ReservationDao reservationDao = new ReservationDao(createDataSource());
        if (reservationDao.findAll().size() != 0) {
            System.out.println("Something is saved");
        }
        new MainWindow(reservationDao);
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/hotel-app";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

}
