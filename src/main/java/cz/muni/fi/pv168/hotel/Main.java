package cz.muni.fi.pv168.hotel;

import cz.muni.fi.pv168.hotel.guests.GuestDao;
import cz.muni.fi.pv168.hotel.gui.MainWindow;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import cz.muni.fi.pv168.hotel.reservations.ReservationStatus;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.awt.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        ReservationDao reservationDao = new ReservationDao(dataSource);
        RoomDao roomDao = null;
        GuestDao guestDao = new GuestDao(dataSource);
//        guestDao.dropTable();
//        reservationDao.dropTable();
//        Reservation tmp = new Reservation("Jeleň", "123", "abcd@gmail.com", 3, new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15}
//                , LocalDate.now(), LocalDate.now().plusDays(5), "PLANNED");
//        reservationDao.printAll(reservationDao.findAll());
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
