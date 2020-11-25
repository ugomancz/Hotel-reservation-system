package cz.muni.fi.pv168.hotel_app.data;


import cz.muni.fi.pv168.hotel_app.reservations.Reservation;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;

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
                     "INSERT INTO RESERVATION (ARRIVAL, DEPARTURE, ROOMNUMBER, HOSTS,NAME,PHONE,EMAIL,STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setDate(1, Date.valueOf(reservation.getArrival()));
            st.setDate(2, Date.valueOf(reservation.getDeparture()));
            st.setLong(3, reservation.getRoomNumber());
            st.setLong(4, reservation.getHosts());
            st.setString(5, reservation.getName());
            st.setString(6, reservation.getPhone());
            st.setString(7, reservation.getEmail());
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

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.RESERVATION (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "ARRIVAL DATE NOT NULL," +
                    "DEPARTURE DATE NOT NULL," +
                    "ROOMNUMBER BIGINT NOT NULL," +
                    "HOSTS BIGINT," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PHONE VARCHAR(100) NOT NULL," +
                    "EMAIL VARCHAR(100)," +
                    "STATUS VARCHAR(100) NOT NULL" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create RESERVATION table", ex);
        }
    }

}
