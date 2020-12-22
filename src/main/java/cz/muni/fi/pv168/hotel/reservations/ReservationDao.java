package cz.muni.fi.pv168.hotel.reservations;

import cz.muni.fi.pv168.hotel.DataAccessException;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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
                     "INSERT INTO RESERVATION (NAME, PHONE, EMAIL, HOSTS, ROOMNUMBER, ARRIVAL, DEPARTURE, STATUS, GUESTID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, reservation.getName());
            st.setString(2, reservation.getPhone());
            st.setString(3, reservation.getEmail());
            st.setLong(4, reservation.getHosts());
            st.setLong(5, reservation.getRoomNumber());
            st.setDate(6, Date.valueOf(reservation.getArrival()));
            st.setDate(7, Date.valueOf(reservation.getDeparture()));
            st.setString(8, reservation.getStatus().name());
            st.setString(9, reservation.getGuestID());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for reservation: " + reservation);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store reservation " + reservation, ex);
        }
    }

    public void delete(Reservation reservation) {
        if (reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM RESERVATION WHERE ID = ?")) {
            st.setLong(1, reservation.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete reservation " + reservation, ex);
        }
    }

    public void update(Reservation reservation) {
        if (reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE RESERVATION SET NAME = ?, PHONE = ?, EMAIL = ?, HOSTS = ?,"
                             + " ROOMNUMBER = ?, ARRIVAL = ?, DEPARTURE = ?, STATUS = ?, GUESTID = ? WHERE ID = ?")) {
            st.setString(1, reservation.getName());
            st.setString(2, reservation.getPhone());
            st.setString(3, reservation.getEmail());
            st.setInt(4, reservation.getHosts());
            st.setInt(5, reservation.getRoomNumber());
            st.setDate(6, Date.valueOf(reservation.getArrival()));
            st.setDate(7, Date.valueOf(reservation.getDeparture()));
            st.setString(8, reservation.getStatus().name());
            st.setString(9, reservation.getGuestID());
            st.setLong(10, reservation.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update reservation " + reservation, ex);
        }
    }

    public void printAll(List<Reservation> list) {
        for (Reservation entry : list) {
            System.out.println(entry);
        }
    }

    public List<Reservation> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PHONE, EMAIL, HOSTS," +
                     " ROOMNUMBER, ARRIVAL, DEPARTURE, STATUS, GUESTID FROM RESERVATION")) {

            List<Reservation> reservations = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getString("NAME"),
                            rs.getString("PHONE"),
                            rs.getString("EMAIL"),
                            rs.getInt("HOSTS"),
                            rs.getInt("ROOMNUMBER"),
                            rs.getDate("ARRIVAL").toLocalDate(),
                            rs.getDate("DEPARTURE").toLocalDate(),
                            rs.getString("STATUS")
                    );
                    reservation.setId(rs.getLong("ID"));
                    reservation.setGuestID(rs.getString("GUESTID"));
                    reservations.add(reservation);
                }
            }
            return reservations;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public List<Reservation> getReservation(int room, LocalDate date) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PHONE, EMAIL, HOSTS,"
                     + " ROOMNUMBER, ARRIVAL, DEPARTURE, STATUS, GUESTID FROM RESERVATION WHERE roomnumber=? AND ((ARRIVAL<=? AND ?<=DEPARTURE))")) {
            st.setInt(1, room);
            st.setDate(2, Date.valueOf(date));
            st.setDate(3, Date.valueOf(date));
            List<Reservation> reservations = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(rs.getString("NAME"), rs.getString("PHONE"),
                            rs.getString("EMAIL"), rs.getInt("HOSTS"), rs.getInt("ROOMNUMBER"),
                            rs.getDate("ARRIVAL").toLocalDate(), rs.getDate("DEPARTURE").toLocalDate(),
                            rs.getString("STATUS"));
                    reservation.setId(rs.getLong("ID"));
                    reservation.setGuestID(rs.getString("GUESTID"));
                    reservations.add(reservation);
                }
            }
            return reservations;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public int getNumOfReservations(LocalDate date) {
        int count = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT count(DISTINCT roomnumber) as totalRows FROM RESERVATION WHERE STATUS<>? "
                     + "AND (" + "(ARRIVAL<=? AND ?<=DEPARTURE)"
                     + ")")) {
            st.setString(1, "PAST");
            st.setDate(2, Date.valueOf(date));
            st.setDate(3, Date.valueOf(date));
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("totalRows");
                }
            }
            return count;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public boolean isFree(int room, LocalDate arrival, LocalDate departure) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT count(*) as totalRows FROM RESERVATION WHERE STATUS<>? AND ROOMNUMBER=? " + "AND ("
                             + "(DEPARTURE>? AND ARRIVAL<?)"
                             + ")")) {
            st.setString(1, "PAST");
            st.setInt(2, room);
            st.setDate(3, Date.valueOf(arrival));
            st.setDate(4, Date.valueOf(departure));


            int result;
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("totalRows");
                } else {
                    result = 0;
                }
            }
            return result == 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public boolean isFree(int room, LocalDate arrival, LocalDate departure, long id) {

        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT count(*) as totalRows FROM RESERVATION WHERE STATUS<>? AND ROOMNUMBER=? " + "AND ("
                             + "(DEPARTURE>? AND ARRIVAL<?) AND ID<>?"

                             + ")")) {
            st.setString(1, "PAST");
            st.setInt(2, room);
            st.setDate(3, Date.valueOf(arrival));
            st.setDate(4, Date.valueOf(departure));
            st.setLong(5, id);


            int result;
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("totalRows");
                } else {
                    result = 0;
                }
            }
            return result == 0;
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
                    "STATUS VARCHAR(100) NOT NULL," +
                    "GUESTID VARCHAR(100))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create RESERVATION table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.RESERVATION");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop RESERVATION table", ex);
        }
    }
}
