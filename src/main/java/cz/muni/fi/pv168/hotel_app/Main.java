package cz.muni.fi.pv168.hotel_app;

import cz.muni.fi.pv168.hotel_app.data.ReservationDao;
import cz.muni.fi.pv168.hotel_app.gui.MainWindow;
import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

public class Main {

    public static ReservationDao reservationDao;
    public static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        reservationDao = new ReservationDao(createDataSource());
        if(reservationDao.findAll().size() != 0){
            System.out.println("Something is saved");
        }
        new MainWindow();
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/employee-evidence";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

}
