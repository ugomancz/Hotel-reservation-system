package cz.muni.fi.pv168.hotel_app.data;


import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


/**
 * @author Denis Kollar
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public final class ReservationDao {
    private final DataSource dataSource;

    public ReservationDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExits("APP", "RESERVATION")) {
            createTable();
        }
    }

    private boolean tableExits(String schemaName, String tableName) {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exist", ex);
        }
    }

    public void create(Reservation reservation) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO RESERVATION (NAME, PHONE, EMAIL, HOSTS, ROOMNUMBER, ARRIVAL, DEPARTURE, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, reservation.getName());
            st.setString(2, reservation.getPhone());
            st.setString(3, reservation.getEmail());
            st.setLong(4, reservation.getHosts());
            st.setLong(5, reservation.getRoomNumber());
            st.setDate(6, Date.valueOf(reservation.getArrival()));
            st.setDate(7, Date.valueOf(reservation.getDeparture()));
            st.setString(8, reservation.getStatus().name());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for reservation: " + reservation);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store employee " + reservation, ex);
        }
    }

    public List<Reservation> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PHONE, EMAIL, HOSTS," +
                     " ROOMNUMBER, ARRIVAL, DEPARTURE, STATUS FROM RESERVATION")) {

            List<Reservation> reservations = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getString("NAME"),
                            rs.getString("PHONE"),
                            rs.getString("EMAIL"),
                            rs.getInt("ROOMNUMBER"),
                            rs.getInt("HOSTS"),
                            rs.getDate("ARRIVAL").toLocalDate(),
                            rs.getDate("DEPARTURE").toLocalDate(),
                            rs.getString("STATUS")
                    );
                    reservation.setId(rs.getLong("ID"));
                    reservations.add(reservation);
                }
            }
            return reservations;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }


    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.RESERVATION (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PHONE VARCHAR(100) NOT NULL," +
                    "EMAIL VARCHAR(100)," +
                    "HOSTS INT," +
                    "ROOMNUMBER INT NOT NULL," +
                    "ARRIVAL DATE NOT NULL," +
                    "DEPARTURE DATE NOT NULL," +
                    "STATUS VARCHAR(100) NOT NULL" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create RESERVATION table", ex);
        }
    }

}
